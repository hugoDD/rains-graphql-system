package com.rains.graphql.arthas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * (ArthasCmdlog)表实体类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("arthas_cmdlog")
public class ArthasCmdlog extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器地址
     */
    private String hostname;
    /**
     * 应用名称
     */
    private String appname;
    /**
     * 命令
     */
    private String cmd;
    /**
     * 操作人员ID
     */
    private String createPersonId;
    /**
     * 操作人员名称
     */
    private String createPersonName;
    /**
     * 创建时间
     */
    @DateTimeFormat
    private Date createTime;

}
