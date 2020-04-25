package com.rains.graphql.system.mutation;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IUserPostService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 用户岗位 Mutation
 * szzd
 *
 * @author hugo
 * @date 2020-03-25
 */
@Slf4j
@Component
public class UserPostMutation implements GraphQLMutationResolver {
    @Autowired
    private BaseMutation mutation;

    @Autowired
    private IUserPostService userPostService;


    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("system:userPost:[#request.opt]")
    public boolean userPostBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        Consumer<QueryRequest> exportOpt = q -> userPostService.export(q, env);
        request.opt("importTable", exportOpt);

        return userPostService.baseOpt(request);
    }
}
