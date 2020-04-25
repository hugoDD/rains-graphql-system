package com.rains.graphql.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.servlet.config.ObjectMapperConfigurer;
import graphql.servlet.core.DefaultObjectMapperConfigurer;

public class JsonMapper {
    private final ObjectMapperConfigurer objectMapperConfigurer = new DefaultObjectMapperConfigurer();
    private volatile ObjectMapper mapper;

    // Double-check idiom for lazy initialization of instance fields.
    public ObjectMapper getJacksonMapper() {
        ObjectMapper result = mapper;
        if (result == null) { // First check (no locking)
            synchronized (this) {
                result = mapper;
                if (result == null) { // Second check (with locking)
                    mapper = result = new ObjectMapper();
                    this.objectMapperConfigurer.configure(mapper);
                }
            }
        }

        return result;
    }

}
