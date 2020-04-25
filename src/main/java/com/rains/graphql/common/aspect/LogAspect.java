package com.rains.graphql.common.aspect;

import com.rains.graphql.common.authentication.JWTUtil;
import com.rains.graphql.common.properties.RainsGraphqlProperties;
import com.rains.graphql.common.utils.HttpContextUtil;
import com.rains.graphql.common.utils.IPUtil;
import com.rains.graphql.system.domain.Log;
import com.rains.graphql.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * AOP 记录用户操作日志
 *
 * @author MrBird
 * @link https://mrbird.cc/Spring-Boot-AOP%20log.html
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final TemplateParserContext templateParserContext = new TemplateParserContext("[", "]");

    @Autowired
    private RainsGraphqlProperties rainsGraphqlProperties;

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.rains.graphql.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();


        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (rainsGraphqlProperties.isOpenAopLog()) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            // 保存日志
            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
            if (StringUtils.isNotBlank(token)) {
                username = JWTUtil.getUsername(token);
            }

            Log log = new Log();
            log.setUsername(username);
            log.setIp(ip);
            log.setTime(time);
            com.rains.graphql.common.annotation.Log logAnnotation = method.getAnnotation(com.rains.graphql.common.annotation.Log.class);
            if (logAnnotation != null) {
                EvaluationContext evaluationContext = new MethodBasedEvaluationContext(null, signature.getMethod(), point.getArgs(), paramNameDiscoverer);
                Expression expression = this.parser.parseExpression(logAnnotation.value(), templateParserContext);
                // 注解上的描述
                log.setOperation(expression.getValue(evaluationContext, String.class));
            }
            logService.saveLog(point, log);
        }
        return result;
    }
}
