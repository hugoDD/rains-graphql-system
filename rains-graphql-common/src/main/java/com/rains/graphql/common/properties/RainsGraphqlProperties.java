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

    private static String profile;
    private ShiroProperties shiro = new ShiroProperties();
    private boolean openAopLog = true;
    private SwaggerProperties swagger = new SwaggerProperties();

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String pro) {
        profile = pro;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

}
