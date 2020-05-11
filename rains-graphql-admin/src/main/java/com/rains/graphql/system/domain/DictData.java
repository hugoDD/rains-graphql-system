package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典数据表 Entity
 *
 * @author hugo
 * @date 2020-02-12 10:20:28
 */
@Data
@TableName("sys_dict_data")
@Excel("字典数据表")
public class DictData implements Serializable {

    /**
     * 字典编码
     */
    @ExcelField(value = "字典编码")
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /**
     * 创建者
     */
    @ExcelField(value = "创建者")
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 样式属性（其他样式扩展）
     */
    @ExcelField(value = "样式属性")
    @TableField("css_class")
    private String cssClass;


    /**
     * 字典标签
     */
    @ExcelField(value = "字典标签")
    @TableField("dict_label")
    private String dictLabel;

    /**
     * 字典排序
     */
    @ExcelField(value = "")
    @TableField("dict_sort")
    private Integer dictSort;

    /**
     * 字典类型
     */
    @ExcelField(value = "字典类型")
    @TableField("dict_type")
    private String dictType;

    /**
     * 字典键值
     */
    @ExcelField(value = "字典键值")
    @TableField("dict_value")
    private String dictValue;

    /**
     * 是否默认（Y是 N否）
     */
    @ExcelField(value = "是否默认", writeConverterExp = "Y=是,N=否")
    @TableField("is_default")
    private String isDefault;

    /**
     * 表格回显样式
     */
    @ExcelField(value = "表格回显样式")
    @TableField("list_class")
    private String listClass;

    /**
     * 备注
     */
    @ExcelField(value = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelField(value = "状态", writeConverterExp = "0=正常,1=停用")
    @TableField("status")
    private String status;

    /**
     * 更新者
     */
    @ExcelField(value = "更新者")
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelField(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

}
