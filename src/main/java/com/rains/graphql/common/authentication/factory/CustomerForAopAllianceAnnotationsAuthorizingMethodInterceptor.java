package com.rains.graphql.common.authentication.factory;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.*;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbzhongsy on 2018/8/27.
 */
public class CustomerForAopAllianceAnnotationsAuthorizingMethodInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor {

    public CustomerForAopAllianceAnnotationsAuthorizingMethodInterceptor() {
        List<AuthorizingAnnotationMethodInterceptor> interceptors = new ArrayList(5);
        AnnotationResolver resolver =new CustomerSpringAnnotationResolver();
        interceptors.add(new RoleAnnotationMethodInterceptor(resolver));
        interceptors.add(new CustomerPermissionAnnotationMethodInterceptor(resolver));
        interceptors.add(new AuthenticatedAnnotationMethodInterceptor(resolver));
        interceptors.add(new UserAnnotationMethodInterceptor(resolver));
        interceptors.add(new GuestAnnotationMethodInterceptor(resolver));
        this.setMethodInterceptors(interceptors);
    }
}
