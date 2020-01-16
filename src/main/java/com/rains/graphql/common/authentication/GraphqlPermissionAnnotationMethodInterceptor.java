package com.rains.graphql.common.authentication;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.apache.shiro.subject.Subject;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.Annotation;

public class GraphqlPermissionAnnotationMethodInterceptor extends PermissionAnnotationMethodInterceptor {

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final TemplateParserContext templateParserContext = new TemplateParserContext("[","]");
    public GraphqlPermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(resolver);
    }
    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        Annotation annotation = super.getAnnotation(mi);
        RequiresPermissions permAnnotation = (RequiresPermissions) annotation;
        String[] perms = permAnnotation.value();
        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(null, mi.getMethod(), mi.getArguments(), paramNameDiscoverer);
        for (int i=0; i<perms.length; i++) {
            Expression expression = this.parser.parseExpression(perms[i], templateParserContext);
            //使用Spring EL表达式解析后的权限定义替换原来的权限定义
            perms[i] = expression.getValue(evaluationContext, String.class);
        }
        Subject subject = getSubject();
        if (perms.length == 1) {
            subject.checkPermission(perms[0]);
            return;
        }
        if (Logical.AND.equals(permAnnotation.logical())) {
            getSubject().checkPermissions(perms);
            return;
        }
        if (Logical.OR.equals(permAnnotation.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms) if (getSubject().isPermitted(permission)) hasAtLeastOnePermission = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOnePermission) getSubject().checkPermission(perms[0]);
        }
    }
}
