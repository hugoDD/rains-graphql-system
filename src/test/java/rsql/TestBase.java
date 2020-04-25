package rsql;/*
 *  com.github.rutledgepaulv.rqe.pipes.rsql.TestBase
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import rsql.testsupport.CriteriaSerializer;
import rsql.testsupport.User;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public abstract class TestBase {

    public static RunMode MODE = RunMode.PRINT;

    public void assertElasticsearch(Condition<?> condition, String expected) {
        QueryBuilder criteria = condition.query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());
        String actual = criteria.toString();
        doOrPrint(() -> assertEquals(expected, actual), actual);
    }

    public void assertMongo(Condition<?> condition, String expected) {
        Criteria criteria = condition.query(new MongoVisitor());
        String actual = new CriteriaSerializer().apply(criteria);
        doOrPrint(() -> assertEquals(expected, actual), actual);
    }

    public void assertPredicate(Condition<?> condition, Object object) {
        Predicate<Object> pred = condition.query(new PredicateVisitor<>());
        assertTrue(pred.test(object));
    }

    public void assertNotPredicate(Condition<?> condition, Object object) {
        Predicate<Object> pred = condition.query(new PredicateVisitor<>());
        assertFalse(pred.test(object));
    }

    public void assertRsql(Condition<?> condition, Object object) {
        String pred = condition.query(new RSQLVisitor());
        assertEquals(pred, object);
    }

    public void assertMybatisPlus(Condition<?> condition, QueryWrapper object, String rsql) {
        MybatisPlusQueryVisitor<User> visitor = new MybatisPlusQueryVisitor<>();
        MybatisPlusQueryVisitor.Context<User> context = new MybatisPlusQueryVisitor.Context<>();
        Wrapper<User> rootWrapper = condition.query(visitor, context);
        Wrapper<User> converted = context.getWrapper().mergeNested(rootWrapper);
        logSqlSegment("rsql：" + rsql, converted);
        logParams((QueryWrapper<User>) converted);
        assertEquals(StringUtils.deleteWhitespace(converted.getSqlSegment()), StringUtils.deleteWhitespace(object.getSqlSegment()));
        assertEquals(((QueryWrapper<User>) converted).getParamNameValuePairs(), object.getParamNameValuePairs());
    }

    private void logSqlSegment(String explain, ISqlSegment sqlSegment) {
        System.out.println(String.format(" ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓   ->[%s]<-   ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓", explain));
        System.out.println("where:  " + sqlSegment.getSqlSegment());
    }

    private <T> void logParams(QueryWrapper<T> wrapper) {
        wrapper.getParamNameValuePairs().forEach((k, v) ->
                System.out.println("key: '" + k + "'\t\tvalue: '" + v + StringPool.SINGLE_QUOTE));
    }

    public void doOrPrint(Runnable doMe, String printMe) {
        if (MODE == RunMode.TEST) {
            doMe.run();
        } else {
            System.out.println(printMe);
        }
    }


    public enum RunMode {
        TEST,
        PRINT
    }

}
