package com.oembedler.moon.graphql.boot.internal;

import com.oembedler.moon.graphql.boot.input.MyGraphQLInvocationInputFactory;
import graphql.servlet.core.GraphQLObjectMapper;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.core.SubscriptionConnectionListener;

import java.util.Optional;

public class MySubscriptionHandlerInput  {
    private final MyGraphQLInvocationInputFactory invocationInputFactory;
    private final GraphQLQueryInvoker queryInvoker;
    private final GraphQLObjectMapper graphQLObjectMapper;
    private final SubscriptionConnectionListener subscriptionConnectionListener;

    public MySubscriptionHandlerInput(MyGraphQLInvocationInputFactory invocationInputFactory, GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper, SubscriptionConnectionListener subscriptionConnectionListener) {
        this.invocationInputFactory = invocationInputFactory;
        this.queryInvoker = queryInvoker;
        this.graphQLObjectMapper = graphQLObjectMapper;
        this.subscriptionConnectionListener = subscriptionConnectionListener;
    }

    public MyGraphQLInvocationInputFactory getInvocationInputFactory() {
        return invocationInputFactory;
    }

    public GraphQLQueryInvoker getQueryInvoker() {
        return queryInvoker;
    }

    public GraphQLObjectMapper getGraphQLObjectMapper() {
        return graphQLObjectMapper;
    }

    public Optional<SubscriptionConnectionListener> getSubscriptionConnectionListener() {
        return Optional.ofNullable(subscriptionConnectionListener);
    }
}
