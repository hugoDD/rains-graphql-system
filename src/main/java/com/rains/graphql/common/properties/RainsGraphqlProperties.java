package com.rains.graphql.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "rains")
public class RainsGraphqlProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

    private SwaggerProperties swagger = new SwaggerProperties();
}
