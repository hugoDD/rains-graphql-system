package com.rains.graphql.tool.service.impl;

import com.rains.graphql.tool.entity.GenTableColumn;
import com.rains.graphql.tool.mapper.GenTableColumnMapper;
import com.rains.graphql.tool.service.IGenTableColumnService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 代码生成业务表字段 Service实现
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
@Service
@Transactional
public class GenTableColumnServiceImpl extends BaseService<GenTableColumnMapper, GenTableColumn> implements IGenTableColumnService {

    @Override
    public List<GenTableColumn> findGenTableColumns(GenTableColumn genTableColumn) {
	    LambdaQueryWrapper<GenTableColumn> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }


}
