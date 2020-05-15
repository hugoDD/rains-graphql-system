package com.rains.graphql.arthas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * (ArthasMachineinfo)表实体类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("arthas_machineinfo")
@Excel("服务器信息")
public class ArthasMachineinfo extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "ID")
    private Integer id;
    @ExcelField(value = "主机")
    private String hostname;

    /**
     * 登录类型
     */
    @ExcelField(value = "登录类型")
    private String loginType;
    /**
     * RSA
     */
    @ExcelField(value = "RSA")
    private Integer rsaId;
    /**
     * 登陆账号
     */
    @ExcelField(value = "登陆账号")
    private String username;
    /**
     * 密码
     */
    @ExcelField(value = "密码")
    private String password;
    /**
     * SSH端口号
     */
    @ExcelField(value = "SSH端口号")
    private Integer port;
    /**
     * agentIP
     */
    @ExcelField(value = "agentIP")
    private String arthasIp;
    /**
     * agent端口
     */
    @ExcelField(value = "agent端口")
    private Integer arthasPort;
    /**
     * agentId
     */
    @ExcelField(value = "agentId")
    private String arthasAgentId;
    /**
     * 服务器连接状态
     */
    @ExcelField(value = "服务器连接状态")
    private String serverStatus;
    /**
     * 模块名称
     */
    @ExcelField(value = "模块名称")
    private String moduleName;
    /**
     * 状态
     */
    @ExcelField(value = "状态")
    private String status;


}
