package com.rains.graphql.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rains.graphql.tool.entity.GenTableColumn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 代码生成业务表字段 Mapper
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    @Select("\tselect column_name, (case when (is_nullable = 'no' &&  column_key != 'PRI') then '1' else null end) as is_required, (case when column_key = 'PRI' then '1' else '0' end) as is_pk, ordinal_position as sort, column_comment, (case when extra = 'auto_increment' then '1' else '0' end) as is_increment, column_type\n" +
            "\t\tfrom information_schema.columns where table_schema = (select database()) and table_name = (#{tableName})\n" +
            "\t\torder by ordinal_position")
    List<GenTableColumn> selectDbTableColumnsByName(@Param("tableName") String tableName);


}
