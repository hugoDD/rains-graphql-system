package com.rains.graphql.system.query;

import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Dict;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Validated
@Component
public class DictQuery  implements GraphQLQueryResolver {


    @Autowired
    private DictService dictService;

    @GetMapping
    @RequiresPermissions("dict:view")
    public PageData<Dict> dictPage(QueryRequest request, Dict dict) {
        IPage<Dict> page =this.dictService.findDicts(request, dict);
        return new PageData<>(page.getTotal(),page.getRecords());
    }
    @RequiresPermissions("dict:detail")
    public Dict dictDetail( Dict dict) {
        return dictService.getOne(new QueryWrapper<>(dict));
    }




}
