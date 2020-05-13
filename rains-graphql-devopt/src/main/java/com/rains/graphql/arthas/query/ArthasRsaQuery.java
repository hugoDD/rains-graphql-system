package com.rains.graphql.arthas.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.arthas.entity.ArthasRsa;
import com.rains.graphql.arthas.service.ArthasRsaService;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * (ArthasRsa)表控制层
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Slf4j
@Component
public class ArthasRsaQuery implements GraphQLQueryResolver {
    /**
     * 服务对象
     */
    @Autowired
    private ArthasRsaService arthasRsaService;

    @RequiresPermissions("arthasRsa:view")
    public PageData<ArthasRsa> arthasRsaPage(QueryRequest request) {
        return arthasRsaService.query(request);
    }


}
