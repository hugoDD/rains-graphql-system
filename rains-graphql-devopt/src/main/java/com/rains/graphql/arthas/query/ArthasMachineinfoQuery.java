package com.rains.graphql.arthas.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * (ArthasMachineinfo)表控制层
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Slf4j
@Component
public class ArthasMachineinfoQuery implements GraphQLQueryResolver {
    /**
     * 服务对象
     */
    @Autowired
    private ArthasMachineinfoService arthasMachineinfoService;

    @RequiresPermissions("arthas:machineinfo:view")
    public PageData<ArthasMachineinfo> arthasMachineinfoPage(QueryRequest request) {
        return arthasMachineinfoService.query(request);
    }


}
