package com.rains.graphql.common.graphql.internal;

import com.corundumstudio.socketio.SocketIOClient;
import graphql.ExecutionResult;
import graphql.servlet.core.internal.SubscriptionProtocolHandler;
import graphql.servlet.core.internal.WsSessionSubscriptions;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MySubscriptionProtocolHandler {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionProtocolHandler.class);

    public abstract void onMessage(SocketIOClient session, WsSessionSubscriptions subscriptions, String text) throws Exception;

    protected abstract void sendDataMessage(SocketIOClient session, String id, Object payload);

    protected abstract void sendErrorMessage(SocketIOClient session, String id);

    protected abstract void sendCompleteMessage(SocketIOClient session, String id);

    protected void subscribe(SocketIOClient session, ExecutionResult executionResult, WsSessionSubscriptions subscriptions, String id) {
        final Object data = executionResult.getData();

        if (data instanceof Publisher) {
            @SuppressWarnings("unchecked") final Publisher<ExecutionResult> publisher = (Publisher<ExecutionResult>) data;
            final MySubscriptionProtocolHandler.AtomicSubscriptionReference subscriptionReference = new MySubscriptionProtocolHandler.AtomicSubscriptionReference();

            publisher.subscribe(new Subscriber<ExecutionResult>() {
                @Override
                public void onSubscribe(Subscription subscription) {
                    subscriptionReference.set(subscription);
                    subscriptionReference.get().request(1);

                    subscriptions.add(id, subscriptionReference.get());
                }

                @Override
                public void onNext(ExecutionResult executionResult) {
                    subscriptionReference.get().request(1);
                    Map<String, Object> result = new HashMap<>();
                    result.put("data", executionResult.getData());
                    sendDataMessage(session, id, result);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("Subscription error", throwable);
                    unsubscribe(subscriptions, id);
                    sendErrorMessage(session, id);
                }

                @Override
                public void onComplete() {
                    unsubscribe(subscriptions, id);
                    sendCompleteMessage(session, id);
                }
            });
        }
    }

    protected void unsubscribe(WsSessionSubscriptions subscriptions, String id) {
        subscriptions.cancel(id);
    }

    static class AtomicSubscriptionReference {
        private final AtomicReference<Subscription> reference = new AtomicReference<>(null);

        public void set(Subscription subscription) {
            if(reference.get() != null) {
                throw new IllegalStateException("Cannot overwrite subscription!");
            }

            reference.set(subscription);
        }

        public Subscription get() {
            Subscription subscription = reference.get();
            if(subscription == null) {
                throw new IllegalStateException("Subscription has not been initialized yet!");
            }

            return subscription;
        }
    }
}
