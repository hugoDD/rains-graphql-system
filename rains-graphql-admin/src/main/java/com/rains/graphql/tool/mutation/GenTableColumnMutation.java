package com.rains.graphql.tool.mutation;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.tool.service.IGenTableColumnService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 代码生成业务表字段 Mutation
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
@Slf4j
@Component
public class GenTableColumnMutation implements GraphQLMutationResolver {


    @Autowired
    private IGenTableColumnService genTableColumnService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("tool:gen:[#request.opt]")
    public boolean genTableColumnBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        switch (request.getOpt()) {
            case "export":
                genTableColumnService.export(request, env);
                return true;

            default:
                return genTableColumnService.baseOpt(request);
        }
    }


}
