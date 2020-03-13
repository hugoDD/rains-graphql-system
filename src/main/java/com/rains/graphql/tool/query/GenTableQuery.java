package com.rains.graphql.tool.query;


import com.rains.graphql.tool.entity.GenTable;
import com.rains.graphql.tool.service.IGenTableService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 代码生成业务表 QueryResolver
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:07
 */
@Slf4j
@Component
public class GenTableQuery implements GraphQLQueryResolver {

    @Autowired
    private IGenTableService genTableService;

    @RequiresPermissions("tool:gen:query")
    public PageData<GenTable> genTablePage(QueryRequest request) {
        if("dbTable".equals(request.getOpt())){
            List<GenTable> list = genTableService.selectDbTableList(request);
            PageData<GenTable> pageData = new PageData<>(list.size(),list);
            return pageData;
        }
        return genTableService.query(request);
    }



}
