package com.rains.graphql.system.service;


import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Column;
import com.rains.graphql.system.domain.TableDomain;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author hugoDD
 */
public interface GeneratorService {

    List<String> getDatabases(String databaseType);

    IPage<TableDomain> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
