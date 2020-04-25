package com.rains.graphql.tool.service;

import com.rains.graphql.system.service.IBaseService;
import com.rains.graphql.tool.entity.GenTableColumn;

import java.util.List;

/**
 * 代码生成业务表字段 Service接口
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
public interface IGenTableColumnService extends IBaseService<GenTableColumn> {

    /**
     * 查询（所有）
     *
     * @param genTableColumn genTableColumn
     * @return List<GenTableColumn>
     */
    List<GenTableColumn> findGenTableColumns(GenTableColumn genTableColumn);


}
