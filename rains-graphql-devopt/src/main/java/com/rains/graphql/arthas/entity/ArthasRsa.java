package com.rains.graphql.arthas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * (ArthasRsa)表实体类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("arthas_rsa")
public class ArthasRsa extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 私钥名称
     */
    private String rsaName;
    /**
     * 私钥内容
     */
    private String rsaValue;
    /**
     * 私钥用户
     */
    private String rsaUsername;
    /**
     * 私钥密码
     */
    private String rsaPassword;
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
     * 创建人员姓名
     */
    private String createPersonName;

}
