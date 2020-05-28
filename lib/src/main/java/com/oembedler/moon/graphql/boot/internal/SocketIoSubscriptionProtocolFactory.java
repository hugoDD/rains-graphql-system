package com.oembedler.moon.graphql.boot.internal;

import graphql.servlet.core.internal.SubscriptionHandlerInput;

/**
 * @author Andrew Potter
 */
public class SocketIoSubscriptionProtocolFactory extends MySubscriptionProtocolFactory {
    private final MySubscriptionHandlerInput subscriptionHandlerInput;

    public SocketIoSubscriptionProtocolFactory(MySubscriptionHandlerInput subscriptionHandlerInput) {
        super("");
        this.subscriptionHandlerInput = subscriptionHandlerInput;
    }

    @Override
    public MySubscriptionProtocolHandler createHandler() {
        return new SocketIoSubscriptionProtocolHandler(subscriptionHandlerInput);
    }
}
