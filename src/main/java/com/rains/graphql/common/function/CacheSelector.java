package com.rains.graphql.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
