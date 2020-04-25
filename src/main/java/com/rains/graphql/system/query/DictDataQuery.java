package com.rains.graphql.system.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.DictData;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.IDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字典数据表 QueryResolver
 *
 * @author hugo
 * @date 2020-02-12 10:20:28
 */
@Slf4j
@Component
public class DictDataQuery implements GraphQLQueryResolver {

    @Autowired
    private IDictDataService dictDataService;

    @RequiresPermissions("dictData:view")
    public PageData<DictData> dictDataPage(QueryRequest request) {
        return dictDataService.query(request);
    }


}
