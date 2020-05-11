package com.rains.graphql.common.rsql;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MybatisPlusQueryVisitor;
import com.github.rutledgepaulv.rqe.conversions.SpringConversionServiceConverter;
import com.github.rutledgepaulv.rqe.conversions.StringToTypeConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToInstantConverter;
import com.github.rutledgepaulv.rqe.conversions.parsers.StringToObjectBestEffortConverter;
import com.github.rutledgepaulv.rqe.pipes.DefaultArgumentConversionPipe;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.rains.graphql.common.rsql.rqe.conversions.StringToDateTimeConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.StringUtils;

public class RsqlToMybatisPlusWrapper {

    private static RsqlToMybatisPlusWrapper rsqlToMybatisPlusWrapper = new RsqlToMybatisPlusWrapper();
    private QueryConversionPipeline pipeline;

    private RsqlToMybatisPlusWrapper() {
        DefaultArgumentConversionPipe defaultArgumentConversionPipe = DefaultArgumentConversionPipe.builder().useNonDefaultStringToTypeConverter(buildStringToTypeConverTer()).build();
        pipeline = QueryConversionPipeline.builder().useNonDefaultArgumentConversionPipe(defaultArgumentConversionPipe).build();
    }

    public static RsqlToMybatisPlusWrapper getInstance() {
        synchronized (rsqlToMybatisPlusWrapper) {
            return rsqlToMybatisPlusWrapper;
        }
    }

    public <T> Wrapper<T> rsqlToWrapper(String filter, Class<T> clazz) {
        return rsqlToWrapper(filter, clazz, false);
    }

    public <T> Wrapper<T> rsqlToWrapper(String filter, Class<T> clazz, boolean isUpdate) {
        if (StringUtils.isEmpty(filter)) {
            return new QueryWrapper<>();
        }

        //QueryRequest<T> q = new QueryRequest<>();
        Condition<GeneralQueryBuilder> condition = pipeline.apply(filter, clazz);

        MybatisPlusQueryVisitor<T> visitor = new MybatisPlusQueryVisitor<>();
        MybatisPlusQueryVisitor.Context<T> context = new MybatisPlusQueryVisitor.Context<>(isUpdate);
        Wrapper<T> rootWrapper = condition.query(visitor, context);
        Wrapper<T> converted = context.getWrapper().mergeNested(rootWrapper);
        logSqlSegment("rsql：" + filter, converted);
        logParams((AbstractWrapper) converted);
        return converted;
    }

    public StringToTypeConverter buildStringToTypeConverTer() {
        DefaultConversionService conversions = new DefaultConversionService();
        conversions.addConverter(new StringToInstantConverter());
        conversions.addConverter(new StringToObjectBestEffortConverter());
        conversions.addConverter(new StringToDateTimeConverter());
        return new SpringConversionServiceConverter(conversions);
    }


    private void logSqlSegment(String explain, ISqlSegment sqlSegment) {
        System.out.println(String.format(" ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓   ->[%s]<-   ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓", explain));
        System.out.println("where:  " + sqlSegment.getSqlSegment());
    }

    private <T> void logParams(AbstractWrapper wrapper) {
        wrapper.getParamNameValuePairs().forEach((k, v) ->
                System.out.println("key: '" + k + "'\t\tvalue: '" + v + StringPool.SINGLE_QUOTE));
    }
}
