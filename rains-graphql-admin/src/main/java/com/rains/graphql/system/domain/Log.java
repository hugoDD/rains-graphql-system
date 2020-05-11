package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@TableName("sys_log")
@Excel("系统日志表")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户
     */
    @TableField("USERNAME")
    @ExcelField(value = "操作用户")
    private String username;

    /**
     * 操作内容
     */
    @TableField("OPERATION")
    @ExcelField(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    @TableField("TIME")
    @ExcelField(value = "耗时（毫秒）")
    private Long time;

    /**
     * 操作方法
     */
    @TableField("METHOD")
    @ExcelField(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    @TableField("PARAMS")
    @ExcelField(value = "方法参数")
    private String params;

    /**
     * 操作者IP
     */
    @TableField("IP")
    @ExcelField(value = "操作者IP")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 操作地点
     */
    @TableField("LOCATION")
    @ExcelField(value = "操作地点")
    private String location;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @ExcelField(value = "业务类型", writeConverterExp = "0=其它,1=新增,2=修改,3=删除")
    private int type;

    /**
     * 登录状态（0成功 1失败）
     */
    @ExcelField(value = "登录状态", writeConverterExp = "1=成功,0=失败")
    private String status;
}
