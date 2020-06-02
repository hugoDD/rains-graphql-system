package com.rains.graphql.common.graphql.internal;

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
