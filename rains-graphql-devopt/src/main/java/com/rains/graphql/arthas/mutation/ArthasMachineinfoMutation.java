package com.rains.graphql.arthas.mutation;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * (ArthasMachineinfo)表控制层
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Slf4j
@Component
public class ArthasMachineinfoMutation implements GraphQLMutationResolver {
    /**
     * 服务对象
     */
    @Autowired
    private ArthasMachineinfoService arthasMachineinfoService;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("arthasMachineinfo:[#request.opt]")
    public boolean arthasMachineinfoBaseMutation(QueryRequest request, DataFetchingEnvironment env) {

        if ("importTable".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> arthasMachineinfoService.export(q, env);
            request.opt(request.getOpt(), exportOpt);
        }


        return arthasMachineinfoService.baseOpt(request);
    }


}
