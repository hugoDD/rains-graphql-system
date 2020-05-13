package com.rains.graphql.arthas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * (ArthasMachineinfoPrivilege)表实体类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("arthas_machineinfo_privilege")
public class ArthasMachineinfoPrivilege extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器ID
     */
    private Integer machineId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 创建时间
     */
    private Date createTime;

}
