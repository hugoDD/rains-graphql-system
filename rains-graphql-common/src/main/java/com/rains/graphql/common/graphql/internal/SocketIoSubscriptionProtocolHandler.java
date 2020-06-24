package com.rains.graphql.common.graphql.internal;

import com.corundumstudio.socketio.SocketIOClient;
import graphql.servlet.core.internal.GraphQLRequest;
import graphql.servlet.core.internal.WsSessionSubscriptions;
import graphql.servlet.input.GraphQLSingleInvocationInput;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Andrew Potter
 */
public class SocketIoSubscriptionProtocolHandler extends MySubscriptionProtocolHandler {

    private final MySubscriptionHandlerInput input;
    private final SocketIoSubscriptionSender sender;


    public SocketIoSubscriptionProtocolHandler(MySubscriptionHandlerInput subscriptionHandlerInput) {
        this.input = subscriptionHandlerInput;
        sender = new SocketIoSubscriptionSender(subscriptionHandlerInput.getGraphQLObjectMapper().getJacksonMapper());
    }

    @Override
    public void onMessage(SocketIOClient session, WsSessionSubscriptions subscriptions, String text) throws Exception {
        GraphQLSingleInvocationInput graphQLSingleInvocationInput = createInvocationInput(session, text);
        String eventId =graphQLSingleInvocationInput.getExecutionInput().getOperationName();
        subscribe(
                session,
            input.getQueryInvoker().query(graphQLSingleInvocationInput),
            subscriptions,
                eventId
        );
    }

    private GraphQLSingleInvocationInput createInvocationInput(SocketIOClient session, String text) throws IOException {
        GraphQLRequest graphQLRequest = input.getGraphQLObjectMapper().readGraphQLRequest(text);
//        HandshakeRequest handshakeRequest = (HandshakeRequest) session.getUserProperties()
//                .get(HandshakeRequest.class.getName());
//        GraphQLSingleInvocationInput graphQLSingleInvocationInput =input.getInvocationInputFactory().create(graphQLRequest);


        return input.getInvocationInputFactory().create(graphQLRequest,session);
    }

    @Override
    protected void sendDataMessage(SocketIOClient session, String id, Object payload) {
       //SocketIOClient client = session.getClient(UUID.fromString(id));
        session.set("eventId",id);
        sender.send(session, payload);
    }

    @Override
    protected void sendErrorMessage(SocketIOClient session, String id) {

    }

    @Override
    protected void sendCompleteMessage(SocketIOClient session, String id) {

    }
}
