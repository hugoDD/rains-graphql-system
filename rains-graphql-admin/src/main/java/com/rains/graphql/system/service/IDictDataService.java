package com.rains.graphql.system.service;

import com.rains.graphql.system.domain.DictData;

import java.util.List;

/**
 * 字典数据表 Service接口
 *
 * @author hugo
 * @date 2020-02-12 10:20:28
 */
public interface IDictDataService extends IBaseService<DictData> {

    /**
     * 查询（所有）
     *
     * @param dictData dictData
     * @return List<DictData>
     */
    List<DictData> findDictDatas(DictData dictData);


}
