package com.rains.graphql.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SpringELTest {

    @Test
    public void testVariableExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        context.setVariable("variable", "haha");
        context.setVariable("variable", "haha");
        String result1 = parser.parseExpression("#variable").getValue(context, String.class);
        Assert.assertEquals("haha", result1);

        context = new StandardEvaluationContext("haha");
        String result2 = parser.parseExpression("#root").getValue(context, String.class);
        Assert.assertEquals("haha", result2);
        String result3 = parser.parseExpression("#this").getValue(context, String.class);
        Assert.assertEquals("haha", result3);
    }

    @Test
    public void testColletionEL() {
        //SpEL目前支持所有集合类型的访问
        Collection<Map<String, Object>> collection = new HashSet<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("userId", 123141);
        map1.put("roleId", 12314);
        map1.put("postId", 4342);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("userId", 45432);
        map2.put("roleId", 234);
        map2.put("postId", 234);

        collection.add(map1);
        collection.add(map2);
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("collection", collection);
        int result2 = parser.parseExpression("#collection[0]['userId']").getValue(context2, int.class);
        System.out.println(result2);
//对于任何集合类型通过Iterator来定位元素
        Assert.assertEquals(123141, result2);
    }
}
