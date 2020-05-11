package com.rains.graphql.system.mutation;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IDictDataService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

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
    private IDictDataService dictDataService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("dictData:[#request.opt]")
    public boolean dictDataBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> dictDataService.export(q, env);
            request.opt("export", exportOpt);
        }

        return dictDataService.baseOpt(request);
    }
}
