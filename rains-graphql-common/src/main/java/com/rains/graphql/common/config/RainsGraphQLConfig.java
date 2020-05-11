package com.rains.graphql.common.config;

import com.rains.graphql.common.graphql.CustomGraphQLHttpServlet;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.config.GraphQLConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RainsGraphQLConfig {

    @Primary
    @Bean
    public GraphQLHttpServlet graphQLHttpServlet(GraphQLConfiguration graphQLConfiguration) {
        return CustomGraphQLHttpServlet.with(graphQLConfiguration);
    }
}
