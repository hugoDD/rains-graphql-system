package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.DictType;
import com.rains.graphql.system.dao.DictTypeMapper;
import com.rains.graphql.system.service.IDictTypeService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 字典类型表 Service实现
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
@Service
@Transactional
public class DictTypeServiceImpl extends BaseService<DictTypeMapper, DictType> implements IDictTypeService {

    @Override
    public List<DictType> findDictTypes(DictType dictType) {
	    LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }


}
