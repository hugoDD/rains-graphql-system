package com.rains.graphql.system.mutation;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IDictTypeService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 字典类型表 Mutation
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
@Slf4j
@Component
public class DictTypeMutation implements GraphQLMutationResolver {
    @Autowired
    private IDictTypeService dictTypeService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("dictType:[#request.opt]")
    public boolean dictTypeBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> dictTypeService.export(q, env);
            request.opt("export", exportOpt);
        }

        return dictTypeService.baseOpt(request);
    }
}
