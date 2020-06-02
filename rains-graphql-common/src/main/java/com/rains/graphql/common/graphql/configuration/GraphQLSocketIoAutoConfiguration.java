package com.rains.graphql.common.graphql.configuration;

import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.rains.graphql.common.graphql.context.MyGraphQLContextBuilder;
import com.rains.graphql.common.graphql.handler.GraphQLSocketIoHandler;
import com.rains.graphql.common.graphql.input.MyGraphQLInvocationInputFactory;
import graphql.servlet.config.GraphQLSchemaProvider;
import graphql.servlet.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import java.time.Duration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnProperty(value = "graphql.socketio.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({GraphQLJavaToolsAutoConfiguration.class})
public class GraphQLSocketIoAutoConfiguration {

//    @Value("${graphql.servlet.subscriptions.websocket.path:/subscriptions}")
//    private String websocketPath;
//
//    @Autowired
//    private GraphQLSubscriptionApolloProperties apolloProperties;

    @Bean
    @ConditionalOnMissingBean
    public SubscriptionConnectionListener subscriptionConnectionListener() {
      //  if (!apolloProperties.isKeepAliveEnabled()) {
      //      return ApolloSubscriptionConnectionListener.createWithKeepAliveDisabled();
       // }
        return ApolloSubscriptionConnectionListener.createWithKeepAliveInterval(Duration.ofSeconds(15));
    }

    @Autowired(required = false)
    private GraphQLRootObjectBuilder graphQLRootObjectBuilder;

    @Bean
    @ConditionalOnMissingBean
    public NettySocketConfig nettySocketConfig(){
        return new NettySocketConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public MyGraphQLInvocationInputFactory myInvocationInputFactory(GraphQLSchemaProvider schemaProvider) {
        MyGraphQLInvocationInputFactory.Builder builder = MyGraphQLInvocationInputFactory.newBuilder(schemaProvider);

        if (graphQLRootObjectBuilder != null) {
            builder.withGraphQLRootObjectBuilder(graphQLRootObjectBuilder);
        }


        builder.withGraphQLContextBuilder(new MyGraphQLContextBuilder());


        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSocketIoHandler graphQLSocketIoHandler(MyGraphQLInvocationInputFactory invocationInputFactory, GraphQLQueryInvoker queryInvoker, GraphQLObjectMapper graphQLObjectMapper, SubscriptionConnectionListener subscriptionConnectionListener) {
        return new GraphQLSocketIoHandler(queryInvoker, invocationInputFactory, graphQLObjectMapper, subscriptionConnectionListener);
    }



}
