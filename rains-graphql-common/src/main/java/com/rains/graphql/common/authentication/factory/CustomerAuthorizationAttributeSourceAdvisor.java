package com.rains.graphql.common.authentication.factory;

import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by wbzhongsy on 2018/8/27.
 */
public class CustomerAuthorizationAttributeSourceAdvisor extends AuthorizationAttributeSourceAdvisor {
    private static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES = new Class[]{RequiresPermissions.class, RequiresRoles.class, RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class};
    private static final Logger log = LoggerFactory.getLogger(AuthorizationAttributeSourceAdvisor.class);

    public CustomerAuthorizationAttributeSourceAdvisor() {

        this.setAdvice(new CustomerForAopAllianceAnnotationsAuthorizingMethodInterceptor());
    }


}
