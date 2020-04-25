package com.rains.graphql.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Data
public class QueryRequest<T> implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    private int runOrder = 0;

    private int pageSize = 10;
    private int pageNum = 1;

    private String sortField;
    private String sortOrder;

    private String filter;
    /**
     * query,update,insert,delete,export
     */
    private String opt = "query";

    private String svc;

    private Long[] ids;

    private T data;

    private Object[] datas;

    private List<T> batchdatas;

    private List<QueryRequest> child;

    private Consumer<QueryRequest> consumer;
    private Map<String, Consumer<QueryRequest<T>>> optMap = new ConcurrentHashMap<>();

    public QueryRequest opt(String opt, Consumer<QueryRequest<T>> c) {
        optMap.merge(opt, c, (a, b) -> a.andThen(b));
        return this;
    }

    public void execute() {
        optMap.get(this.opt).accept(this);
    }


}
