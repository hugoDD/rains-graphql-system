package com.rains.graphql.common.graphql.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.rains.graphql.common.graphql.input.MyGraphQLInvocationInputFactory;
import com.rains.graphql.common.graphql.internal.MySubscriptionHandlerInput;
import com.rains.graphql.common.graphql.internal.MySubscriptionProtocolFactory;
import com.rains.graphql.common.graphql.internal.MySubscriptionProtocolHandler;
import com.rains.graphql.common.graphql.internal.SocketIoSubscriptionProtocolFactory;
import graphql.servlet.GraphQLWebsocketServlet;
import graphql.servlet.core.GraphQLObjectMapper;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.core.SubscriptionConnectionListener;
import graphql.servlet.core.internal.SubscriptionProtocolHandler;
import graphql.servlet.core.internal.WsSessionSubscriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.io.EOFException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphQLSocketIoHandler {

    private static final Logger log = LoggerFactory.getLogger(GraphQLWebsocketServlet.class);

    private static final String PROTOCOL_HANDLER_REQUEST_KEY = SubscriptionProtocolHandler.class.getName();

//    private final List<SubscriptionProtocolFactory> subscriptionProtocolFactories;
//    private final SubscriptionProtocolFactory fallbackSubscriptionProtocolFactory;
//
//    private final List<String> allSubscriptionProtocols;

    private final MySubscriptionProtocolFactory mySubscriptionProtocolFactory;
    private final MySubscriptionProtocolHandler subscriptionProtocolHandler;
    private final Map<UUID, WsSessionSubscriptions> sessionSubscriptionCache = new ConcurrentHashMap<>();
    private final MySubscriptionHandlerInput subscriptionHandlerInput;
    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);
    private final AtomicBoolean isShutDown = new AtomicBoolean(false);
    private final Object cacheLock = new Object();

    @Autowired
    private SocketIOServer socketIOServer;

    public GraphQLSocketIoHandler(GraphQLQueryInvoker queryInvoker, MyGraphQLInvocationInputFactory invocationInputFactory, GraphQLObjectMapper graphQLObjectMapper) {
        this(queryInvoker, invocationInputFactory, graphQLObjectMapper, null);
    }

    public GraphQLSocketIoHandler(GraphQLQueryInvoker queryInvoker, MyGraphQLInvocationInputFactory invocationInputFactory, GraphQLObjectMapper graphQLObjectMapper, SubscriptionConnectionListener subscriptionConnectionListener) {
        this.subscriptionHandlerInput = new MySubscriptionHandlerInput(invocationInputFactory, queryInvoker, graphQLObjectMapper, subscriptionConnectionListener);
        mySubscriptionProtocolFactory = new SocketIoSubscriptionProtocolFactory(subscriptionHandlerInput);
        subscriptionProtocolHandler = mySubscriptionProtocolFactory.createHandler();
//        subscriptionProtocolFactories = Collections.singletonList(new ApolloSubscriptionProtocolFactory(subscriptionHandlerInput));
//        fallbackSubscriptionProtocolFactory = new FallbackSubscriptionProtocolFactory(subscriptionHandlerInput);
//        allSubscriptionProtocols = Stream.concat(subscriptionProtocolFactories.stream(), Stream.of(fallbackSubscriptionProtocolFactory))
//                .map(SubscriptionProtocolFactory::getProtocol)
//                .collect(Collectors.toList());
    }

    //监听客户端连接
    @OnConnect
    public void onOpen(SocketIOClient client) {
        final WsSessionSubscriptions subscriptions = new WsSessionSubscriptions();
        synchronized (cacheLock) {
            if (isShuttingDown.get()) {
                throw new IllegalStateException("Server is shutting down!");
            }
            sessionSubscriptionCache.put(client.getSessionId(), subscriptions);
        }
        log.info("Session opened: {}", client.getSessionId());
    }

    //监听名为roomMessageSending的请求事件
    @OnEvent(value = "subMessage")
    public void onEvent(SocketIOClient session, AckRequest ackRequest, String data) {
        if (log.isDebugEnabled()) {
            log.debug("sessionId:{},data:{}", session.getSessionId(), data);
        }
        WsSessionSubscriptions subscriptions = sessionSubscriptionCache.get(session.getSessionId());

        try {
            subscriptionProtocolHandler.onMessage(session, subscriptions, data);
        } catch (Exception e) {
            onError(session, e);
            log.error(e.getMessage(), e);
        }
    }

    @OnDisconnect
    public void onClose(SocketIOClient client) {
        log.info("Session closed: {}", client.getSessionId());
        WsSessionSubscriptions subscriptions;
        synchronized (cacheLock) {
            subscriptions = sessionSubscriptionCache.remove(client.getSessionId());
            client.disconnect();
        }
        if (subscriptions != null) {
            subscriptions.close();
        }
    }


    public void onError(SocketIOClient client, Throwable thr) {
        if (thr instanceof EOFException) {
            log.warn("Session {} was killed abruptly without calling onClose. Cleaning up session", client.getSessionId());
            onClose(client);

        } else {
            log.error("Error in websocket session: {}", client.getSessionId(), thr);
            closeUnexpectedly(client, thr);
        }
    }

    private void closeUnexpectedly(SocketIOClient client, Throwable t) {
        client.disconnect();
        log.error("Error closing websocket session for session: {}", client.getSessionId(), t);

    }



    /**
     * Stops accepting connections and closes all existing connections
     */
    @PreDestroy
    public void beginShutDown() {
        synchronized (cacheLock) {
            try {
                isShuttingDown.set(true);
                Map<UUID, WsSessionSubscriptions> copy = new HashMap<>(sessionSubscriptionCache);

                // Prevent comodification exception since #onClose() is called during session.close(), but we can't necessarily rely on that happening so we close subscriptions here anyway.
                copy.forEach((session, wsSessionSubscriptions) -> {
                    wsSessionSubscriptions.close();
                    socketIOServer.getClient(session).disconnect();
                });

                copy.clear();

                if (!sessionSubscriptionCache.isEmpty()) {
                    log.error("GraphQLWebsocketServlet did not shut down cleanly!");
                    sessionSubscriptionCache.clear();
                }
            } finally {
                socketIOServer.stop();
            }
        }
        isShutDown.set(true);
    }

    /**
     * @return true when shutdown is complete
     */
    public boolean isShutDown() {
        return isShutDown.get();
    }

//    private SubscriptionProtocolFactory getSubscriptionProtocolFactory(List<String> accept) {
//        for (String protocol : accept) {
//            for (SubscriptionProtocolFactory subscriptionProtocolFactory : subscriptionProtocolFactories) {
//                if (subscriptionProtocolFactory.getProtocol().equals(protocol)) {
//                    return subscriptionProtocolFactory;
//                }
//            }
//        }
//
//        return fallbackSubscriptionProtocolFactory;
//    }

    public int getSessionCount() {
        return socketIOServer.getAllClients().size();
        //return sessionSubscriptionCache.size();
    }

    public int getSubscriptionCount() {
        return sessionSubscriptionCache.values().stream()
                .mapToInt(WsSessionSubscriptions::getSubscriptionCount)
                .sum();
    }

}
