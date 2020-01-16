package com.rains.graphql;

import com.rains.graphql.common.graphql.CustomGraphQLHttpServlet;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.config.GraphQLConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.UnknownHostException;


@SpringBootApplication
@EnableTransactionManagement
@EnableMethodCache(basePackages = "cc.mrbird.febs")
@EnableCreateCacheAnnotation
//@EnableScheduling
@EnableAsync
public class RainsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RainsApplication.class)
                .run(args);
    }


    @Primary
    @Bean
    public GraphQLHttpServlet graphQLHttpServlet(GraphQLConfiguration graphQLConfiguration) {
        return CustomGraphQLHttpServlet.with(graphQLConfiguration);
    }

}
