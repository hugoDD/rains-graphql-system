package com.rains.graphql.tool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.utils.StringUtils;
import lombok.Data;

import java.util.Date;

/**
 * 代码生成业务表字段 Entity
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:11
 */
@Data
@TableName("gen_table_column")
public class GenTableColumn {

    /**
     * 编号
     */
    @TableId(value = "column_id", type = IdType.AUTO)
    private Long columnId;

    /**
     * 归属表编号
     */
    @TableField("table_id")
    private Long tableId;

    /**
     * 列名称
     */
    @TableField("column_name")
    private String columnName;

    /**
     * 列描述
     */
    @TableField("column_comment")
    private String columnComment;

    /**
     * 列类型
     */
    @TableField("column_type")
    private String columnType;

    @TableField("graphql_type")
    private String graphqlType;

    /**
     * JAVA类型
     */
    @TableField("java_type")
    private String javaType;

    /**
     * JAVA字段名
     */
    @TableField("java_field")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    @TableField("is_pk")
    private Boolean isPk;

    /**
     * 是否自增（1是）
     */
    @TableField("is_increment")
    private Boolean isIncrement;

    /**
     * 是否必填（1是）
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 是否为插入字段（1是）
     */
    @TableField("is_insert")
    private Boolean isInsert;

    /**
     * 是否编辑字段（1是）
     */
    @TableField("is_edit")
    private Boolean isEdit;

    /**
     * 是否列表字段（1是）
     */
    @TableField("is_list")
    private Boolean isList;

    /**
     * 是否查询字段（1是）
     */
    @TableField("is_query")
    private Boolean isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @TableField("query_type")
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @TableField("html_type")
    private String htmlType;

    /**
     * 字典类型
     */
    @TableField("dict_type")
    private String dictType;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                // BaseEntity
                "createBy", "createTime", "updateBy", "updateTime", "remark",
                // TreeEntity
                "parentName", "parentId", "orderNum", "ancestors");
    }

    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum");
    }

    public boolean isUIEdit() {
        return this.isEdit || this.isInsert;
    }

    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public boolean isPkColumn() {
        return this.isPk;
    }

    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }


}
