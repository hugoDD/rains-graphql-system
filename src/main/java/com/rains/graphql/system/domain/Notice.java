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
 * 公告信息对象 sys_notice
 *
 * @author hugo
 * @date 2020-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Excel("公告信息")
public class Notice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    /**
     * 公告标题
     */
    @TableField("notice_title")
    @ExcelField(value = "公告标题")
    private String noticeTitle;
    /**
     * 公告类型（1通知 2公告）
     */
    @TableField("notice_type")
    @ExcelField(value = "公告类型")
    private String noticeType;
    /**
     * 公告内容
     */
    @TableField("notice_content")
    @ExcelField(value = "公告内容")
    private String noticeContent;
    /**
     * 公告状态（0正常 1关闭）
     */
    @TableField("status")
    @ExcelField(value = "公告状态", writeConverterExp = "0=正常,1=关闭")
    private String status;
    /**
     * 创建者
     */
    @TableField("create_by")
    @ExcelField(value = "创建者")
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
    @TableField("update_time")
    @ExcelField(value = "更新时间")
    private Date updateTime;

}
