package com.rains.graphql.common.authentication.factory;


import com.rains.graphql.common.domain.QueryRequest;
import lombok.SneakyThrows;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;


/**
 * Created by wbzhongsy on 2018/8/27.
 */
public class CustomerSpringAnnotationResolver extends SpringAnnotationResolver {
    public CustomerSpringAnnotationResolver() {

    }

    @SneakyThrows
    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();
        Object[] arguments = mi.getArguments();
        String opt = "query";

        Annotation a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) {
//            if (a instanceof RequiresPermissions) {
//                if (Objects.nonNull(arguments) && arguments.length > 1) {
//                    if (arguments[0] instanceof QueryRequest) {
//                        QueryRequest request = (QueryRequest) arguments[0];
//                        opt = request.getOpt();
//                        // 获取代理处理器
//                        InvocationHandler invocationHandler = Proxy.getInvocationHandler(a);
//                        // 过去私有 memberValues 属性
//                        Field f = invocationHandler.getClass().getDeclaredField("memberValues");
//                        f.setAccessible(true);
//                        // 获取实例的属性map
//                        Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
//                        // 修改属性值
//                        memberValues.put("value", opt);
//
//                    }
//                }
//            }
            return a;
        } else {
            Class<?> targetClass = mi.getThis().getClass();
            m = ClassUtils.getMostSpecificMethod(m, targetClass);
            a = AnnotationUtils.findAnnotation(m, clazz);
            return a != null ? a : AnnotationUtils.findAnnotation(mi.getThis().getClass(), clazz);
        }
    }
}
