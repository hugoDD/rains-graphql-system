package com.rains.graphql.tool.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 代码生成业务表 Entity
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:07
 */
@Data
@TableName("gen_table")
public class GenTable {

    /**
     * 编号
     */
    @TableId(value = "table_id", type = IdType.AUTO)
    private Long tableId;

    /**
     * 表名称
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 表描述
     */
    @TableField("table_comment")
    private String tableComment;

    /**
     * 实体类名称
     */
    @TableField("class_name")
    private String className;

    /**
     * 使用的模板（crud单表操作 tree树表操作）
     */
    @TableField("tpl_category")
    private String tplCategory;

    /**
     * 生成包路径
     */
    @TableField("package_name")
    private String packageName;

    /**
     * 生成模块名
     */
    @TableField("module_name")
    private String moduleName;

    /**
     * 生成业务名
     */
    @TableField("business_name")
    private String businessName;

    /**
     * 生成功能名
     */
    @TableField("function_name")
    private String functionName;

    /**
     * 生成功能作者
     */
    @TableField("function_author")
    private String functionAuthor;

    /**
     * 其它生成选项
     */
    @TableField("options")
    private String options;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    /**
     * 主键信息
     */
    @TableField(exist = false)
    private GenTableColumn pkColumn;

    /**
     * 表列信息
     */
    @TableField(exist = false)
    private List<GenTableColumn> columns;


    /**
     * 树编码字段
     */
    @TableField(exist = false)
    private String treeCode;

    /**
     * 树父编码字段
     */
    @TableField(exist = false)
    private String treeParentCode;

    /**
     * 树名称字段
     */
    @TableField(exist = false)
    private String treeName;


}
