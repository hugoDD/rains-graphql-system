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
 * 字典类型表 Entity
 *
 * @author hugo
 * @date 2020-02-12 10:20:31
 */
@Data
@TableName("sys_dict_type")
@Excel("字典类型表 ")
public class DictType implements Serializable {

    /**
     * 字典主键
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

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
     * 字典名称
     */
    @ExcelField(value = "字典名称")
    @TableField("dict_name")
    private String dictName;

    /**
     * 字典类型
     */
    @ExcelField(value = "字典类型")
    @TableField("dict_type")
    private String dictType;

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
