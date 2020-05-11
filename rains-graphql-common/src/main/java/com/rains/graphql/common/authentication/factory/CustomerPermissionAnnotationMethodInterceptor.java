package com.rains.graphql.common.authentication.factory;


import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

import java.lang.annotation.Annotation;


/**
 * Created by wbzhongsy on 2018/8/29.
 */
public class CustomerPermissionAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    public CustomerPermissionAnnotationMethodInterceptor() {
        super(new CustomerPermissionAnnotationHandler());
    }

    public CustomerPermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new CustomerPermissionAnnotationHandler(), resolver);
    }

    @Override
    protected Annotation getAnnotation(MethodInvocation mi) {
        return this.getResolver().getAnnotation(mi, this.getHandler().getAnnotationClass());
    }

    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((AuthorizingAnnotationHandler) this.getHandler()).assertAuthorized(this.getAnnotation(mi));

        } catch (AuthorizationException var3) {
            if (var3.getCause() == null) {
                var3.initCause(new AuthorizationException("Not authorized to invoke method: " + mi.getMethod()));
            }
            throw var3;
        }
    }
}
