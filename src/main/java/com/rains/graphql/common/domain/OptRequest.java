package com.rains.graphql.common.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class OptRequest<R> {
    private Map<String, Function<QueryRequest, R>> optMapFun = new ConcurrentHashMap<>();


    public OptRequest opt(String opt, Function<QueryRequest, R> c) {
        optMapFun.put(opt, c);
        return this;
    }

    public R executeQuery(QueryRequest request) {
        return optMapFun.get(request.getOpt()).apply(request);
    }
}
