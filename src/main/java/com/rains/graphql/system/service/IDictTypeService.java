package com.rains.graphql.system.service;

import com.rains.graphql.system.domain.DictType;

import com.rains.graphql.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rains.graphql.system.service.IBaseService;


import java.util.List;

/**
 * 字典类型表 Service接口
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
public interface IDictTypeService extends IBaseService<DictType> {

    /**
     * 查询（所有）
     *
     * @param dictType dictType
     * @return List<DictType>
     */
    List<DictType> findDictTypes(DictType dictType);


}
