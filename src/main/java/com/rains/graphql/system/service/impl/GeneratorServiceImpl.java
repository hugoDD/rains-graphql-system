package com.rains.graphql.system.service.impl;


import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.utils.SortUtil;
import com.rains.graphql.system.dao.GeneratorMapper;
import com.rains.graphql.system.domain.Column;
import com.rains.graphql.system.domain.TableDomain;
import com.rains.graphql.system.service.GeneratorService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hugoDD
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public List<String> getDatabases(String databaseType) {
        return generatorMapper.getDatabases(databaseType);
    }

    @Override
    public IPage<TableDomain> getTables(String tableName, QueryRequest request, String databaseType, String schemaName) {
        Page<TableDomain> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", RainsConstant.ORDER_ASC, false);
        return generatorMapper.getTables(page, tableName, databaseType, schemaName);
    }

    @Override
    public List<Column> getColumns(String databaseType, String schemaName, String tableName) {
        return generatorMapper.getColumns(databaseType, schemaName, tableName);
    }
}
