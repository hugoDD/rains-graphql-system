package com.rains.graphql.common.rsql.rqe.conversions;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

public class StringToDateTimeConverter implements Converter<String, Date> {
    public static final String LONG_PATTERNS = "yyyy-MM-dd HH:mm:ss";
    public static final String SHORT_PATTERNS = "yyyy-MM-dd";
    public static final String TIME_PATTERNS = "HH:mm:ss";

    @Override
    public Date convert(String source) {
        try {
            return DateUtils.parseDate(source, LONG_PATTERNS, SHORT_PATTERNS, TIME_PATTERNS);
        } catch (ParseException e) {
            throw new RuntimeException("time parse error", e);
        }


    }
}
