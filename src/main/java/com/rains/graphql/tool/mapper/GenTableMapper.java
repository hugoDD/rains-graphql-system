package com.rains.graphql.tool.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.rains.graphql.tool.entity.GenTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 代码生成业务表 Mapper
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:07
 */
public interface GenTableMapper extends BaseMapper<GenTable> {
    /**
     * 查询据库列表
     *
     * @param genTableWrapper 业务信息
     * @return 数据库表集合
     */
    @Select("select table_name, table_comment, create_time, update_time from information_schema.tables  ${ew.customSqlSegment}")
    List<GenTable> selectDbTableList(@Param(Constants.WRAPPER) Wrapper<GenTable> genTableWrapper);


}
