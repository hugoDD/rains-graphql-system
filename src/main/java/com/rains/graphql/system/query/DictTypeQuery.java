package com.rains.graphql.system.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.DictType;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.IDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字典类型表 QueryResolver
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
@Slf4j
@Component
public class DictTypeQuery implements GraphQLQueryResolver {

    @Autowired
    private IDictTypeService dictTypeService;

    @RequiresPermissions("dictType:view")
    public PageData<DictType> dictTypePage(QueryRequest request) {
        return dictTypeService.query(request);
    }


}
