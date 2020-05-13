package com.rains.graphql.arthas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
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
public class ArthasMachineinfo extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableId(value = "hostname", type = IdType.AUTO)
    private String hostname;

    /**
     * 登录类型
     */
    private String loginType;
    /**
     * RSA
     */
    private Integer rsaId;
    /**
     * 登陆账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * SSH端口号
     */
    private Integer port;
    /**
     * agentIP
     */
    private String arthasIp;
    /**
     * agent端口
     */
    private Integer arthasPort;
    /**
     * agentId
     */
    private String arthasAgentId;
    /**
     * 服务器连接状态
     */
    private String serverStatus;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人员ID
     */
    private String createPersonId;
    /**
     * 操作人员名称
     */
    private String createPersonName;

}
