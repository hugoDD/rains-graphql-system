package com.rains.graphql.common.graphql;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;

import java.io.IOException;

public interface GraphQlSubscription extends GraphQLSubscriptionResolver {
    void close();
}
