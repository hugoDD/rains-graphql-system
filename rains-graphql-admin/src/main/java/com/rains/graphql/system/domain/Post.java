package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 用户岗位对象 sys_post
 *
 * @author hugo
 * @date 2020-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@Excel("岗位信息表")
public class Post extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
     * 岗位编码
     */
    @ExcelField(value = "岗位编码")
    @TableField("post_code")
    private String postCode;
    /**
     * 岗位名称
     */
    @ExcelField(value = "岗位名称")
    @TableField("post_name")
    private String postName;
    /**
     * 显示顺序
     */
    @ExcelField(value = "显示顺序")
    @TableField("post_sort")
    private Long postSort;
    /**
     * 状态（0正常 1停用）
     */
    @ExcelField(value = "状态", writeConverterExp = "0=正常,1=停用")
    @TableField("status")
    private String status;
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
    @ExcelField(value = "备注")
    private String remark;

}
