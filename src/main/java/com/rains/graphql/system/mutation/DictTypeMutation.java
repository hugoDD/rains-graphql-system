package com.rains.graphql.system.mutation;


import com.rains.graphql.system.domain.DictType;
import com.rains.graphql.system.service.IDictTypeService;
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
 * 字典类型表 Mutation
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
@Slf4j
@Component
public class DictTypeMutation implements GraphQLMutationResolver {
    @Autowired
    private BaseMutation mutation;

    @Autowired
    private IDictTypeService dictTypeService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("dictType:[#request.opt]")
    public boolean dictTypeBaseMutation(QueryRequest request, DictType entity, DataFetchingEnvironment env) {
        request.setData(entity);
        return mutation.baseMutation(request, env,dictTypeService);
    }
}
