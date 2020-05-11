package com.rains.graphql.system.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.util.generator.GeneratorConstant;
import com.rains.graphql.system.domain.GeneratorConfig;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.domain.TableDomain;
import com.rains.graphql.system.service.GeneratorConfigService;
import com.rains.graphql.system.service.GeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GeneratorQuery implements GraphQLQueryResolver {
    @Autowired
    private GeneratorConfigService generatorConfigService;

    @Autowired
    private GeneratorService generatorService;

    @RequiresPermissions("gen:config")
    public GeneratorConfig getGeneratorConfig() {
        return generatorConfigService.findGeneratorConfig();
    }

    @RequiresPermissions("gen:generate")
    public PageData<TableDomain> tablesInfo(String tableName, String dataBase, QueryRequest request) {
        IPage<TableDomain> pageData = generatorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, dataBase);
        return new PageData<>(pageData.getTotal(), pageData.getRecords());
    }
}
