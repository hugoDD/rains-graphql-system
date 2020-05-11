package com.rains.graphql.system.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hugoDD
 */
@Data
public class Column implements Serializable {
    /**
     * 名称
     */
    private String name;
    /**
     * 是否为主键
     */
    private Boolean isKey;
    /**
     * 类型
     */
    private String type;
    /**
     * 注释
     */
    private String remark;
    /**
     * 属性名称
     */
    private String field;
}
