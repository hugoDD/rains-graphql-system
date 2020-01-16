package com.rains.graphql.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import graphql.schema.DataFetchingEnvironment;

public interface IBaseService<T> extends IService<T> {
    PageData<T> query(QueryRequest request);

    boolean delete(QueryRequest<T> request);

    boolean saveOrUpdate(QueryRequest<T> request);


    void export(QueryRequest<T> request, DataFetchingEnvironment env);
}
