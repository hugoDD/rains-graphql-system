package com.rains.graphql.common.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.springframework.stereotype.Component;

@Component
public class VersionSubscription implements GraphQLSubscriptionResolver {
    private String version = "6.0";
}
