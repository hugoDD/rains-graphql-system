package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.DictData;
import com.rains.graphql.system.dao.DictDataMapper;
import com.rains.graphql.system.service.IDictDataService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 字典数据表 Service实现
 *
 * @author hugo
 * @date 2020-02-12 10:20:28
 */
@Service
@Transactional
public class DictDataServiceImpl extends BaseService<DictDataMapper, DictData> implements IDictDataService {

    @Override
    public List<DictData> findDictDatas(DictData dictData) {
	    LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }


}
