package com.rains.graphql.tool.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.tool.entity.GenTableColumn;
import com.rains.graphql.tool.service.IGenTableColumnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 代码生成业务表字段 QueryResolver
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
@Slf4j
@Component
public class GenTableColumnQuery implements GraphQLQueryResolver {

    @Autowired
    private IGenTableColumnService genTableColumnService;

    @RequiresPermissions("tool:gen:query")
    public PageData<GenTableColumn> genTableColumnPage(QueryRequest request) {
        return genTableColumnService.query(request);
    }


}
