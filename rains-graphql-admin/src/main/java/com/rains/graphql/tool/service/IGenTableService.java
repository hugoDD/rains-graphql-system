package com.rains.graphql.tool.service;

import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IBaseService;
import com.rains.graphql.tool.entity.GenTable;

import java.util.List;
import java.util.Map;

/**
 * 代码生成业务表 Service接口
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:07
 */
public interface IGenTableService extends IBaseService<GenTable> {

    /**
     * 查询（所有）
     *
     * @param genTable genTable
     * @return List<GenTable>
     */
    List<GenTable> findGenTables(GenTable genTable);

    /**
     * 查询据库列表
     *
     * @param request 业务信息
     * @return 数据库表集合
     */
    List<GenTable> selectDbTableList(QueryRequest request);


    boolean importGenTable(List<GenTable> tableList);

    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    Map<String, String> previewCode(Long tableId);

    List<Map<String, Object>> preViewUI(Long tableId, Map<String, Map<String, Object>> uiMap);

}
