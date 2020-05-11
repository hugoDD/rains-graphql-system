package com.rains.graphql;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableMethodCache(basePackages = "com.rains.graphql.system")
@EnableCreateCacheAnnotation
//@EnableScheduling
@EnableAsync
public class RainsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RainsApplication.class)
                .run(args);
    }


}
