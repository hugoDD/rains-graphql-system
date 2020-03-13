package com.rains.graphql.system.mutation;


import com.rains.graphql.system.domain.DictData;
import com.rains.graphql.system.service.IDictDataService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.mutation.BaseMutation;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字典数据表 Mutation
 *
 * @author hugo
 * @date 2020-02-12 10:20:28
 */
@Slf4j
@Component
public class DictDataMutation implements GraphQLMutationResolver {
    @Autowired
    private BaseMutation mutation;

    @Autowired
    private IDictDataService dictDataService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("dictData:[#request.opt]")
    public boolean dictDataBaseMutation(QueryRequest request, DictData entity, DataFetchingEnvironment env) {
        request.setData(entity);
        return mutation.baseMutation(request, env,dictDataService);
    }
}
