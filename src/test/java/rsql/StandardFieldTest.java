/*
 *  com.github.rutledgepaulv.rqe.pipes.StandardFieldTest
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package rsql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.junit.Test;
import rsql.testsupport.User;

public class StandardFieldTest extends TestBase {

    static {
        MODE = RunMode.TEST;
    }

    private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    @Test
    public void stringPropertyOderBy() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul,firstName==asc", User.class);

        assertMybatisPlus(condition, Wrappers.query(User.class).nested(i -> i.eq("firstName", "Paul")).orderByAsc("firstName"), "firstName==Paul,firstName==asc");

    }

    @Test
    public void stringPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul", User.class);

        assertMybatisPlus(condition, Wrappers.query(User.class).eq("firstName", "Paul"), "firstName==Paul");

        assertPredicate(condition, new User().setFirstName("Paul"));

        assertNotPredicate(condition, new User().setFirstName("Joe"));

        assertMongo(condition, "{\"firstName\": \"Paul\"}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"firstName\" : {\n" +
                "      \"value\" : \"Paul\",\n" +
                "      \"boost\" : 1.0\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }


    @Test
    public void stringPropertyOnRootObjectRegex() {

        Condition<GeneralQueryBuilder> condition1 = pipeline.apply("firstName=re=.*Paul$", User.class);


        assertMybatisPlus(condition1, Wrappers.query(User.class).apply("firstName REGEXP {0}", ".*Paul$"), "firstName=re=.*Paul$");

        assertPredicate(condition1, new User().setFirstName("Paul"));

        assertNotPredicate(condition1, new User().setFirstName("Joe"));

        assertMongo(condition1, "{\"firstName\": {\"$regularExpression\": {\"pattern\": \".*Paul$\", \"options\": \"\"}}}");

        assertElasticsearch(condition1, "{\n" +
                "  \"regexp\" : {\n" +
                "    \"firstName\" : {\n" +
                "      \"value\" : \".*Paul$\",\n" +
                "      \"flags_value\" : 65535,\n" +
                "      \"max_determinized_states\" : 10000,\n" +
                "      \"boost\" : 1.0\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void numberPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("age==23", User.class);

        assertMybatisPlus(condition, Wrappers.query(User.class).eq("age", 23), "age==23");


        assertPredicate(condition, new User().setAge(23));

        assertNotPredicate(condition, new User().setAge(24));

        assertMongo(condition, "{\"age\": 23}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"age\" : {\n" +
                "      \"value\" : 23,\n" +
                "      \"boost\" : 1.0\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void booleanPropertyOnRootObject() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("enabled==true", User.class);

        assertMybatisPlus(condition, Wrappers.query(User.class).eq("enabled", true), "enabled==true");

        assertPredicate(condition, new User().setEnabled(true));

        assertNotPredicate(condition, new User().setEnabled(false));

        assertMongo(condition, "{\"enabled\": true}");

        assertElasticsearch(condition, "{\n" +
                "  \"term\" : {\n" +
                "    \"enabled\" : {\n" +
                "      \"value\" : true,\n" +
                "      \"boost\" : 1.0\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void nestedQuery() {

        Condition<GeneralQueryBuilder> condition = pipeline.apply("comments=q='comment==\"This is my first comment\"';timestamp=ex=true", User.class);
        assertRsql(condition, "(comments=q='comment==\"This is my first comment\"';timestamp=ex=\"true\")");
//        MergeQueryWrapper<User> mw =new MergeQueryWrapper<>();
//        QueryWrapper<User> q = new QueryWrapper<User>().exists("timestamp");
//        mw.mergeNested(q);
        assertMybatisPlus(condition, new QueryWrapper<User>().nested(i -> i.exists("timestamp")), "comments=q='comment==\"This is my first comment\"';timestamp=ex=true");
    }

}
