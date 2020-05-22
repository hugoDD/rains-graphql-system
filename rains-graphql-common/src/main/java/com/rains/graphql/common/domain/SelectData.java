package com.rains.graphql.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectData implements Serializable {

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典键值
     */
    private String dictValue;
}
