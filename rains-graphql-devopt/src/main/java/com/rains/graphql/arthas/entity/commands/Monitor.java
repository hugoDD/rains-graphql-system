package com.rains.graphql.arthas.entity.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Monitor extends BaseCommand {
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * Java类
     */
    private String clazz;
    /**
     * 方法（构造方法、普通方法）
     */
    private String method;
    /**
     * 调用次数
     */
    private String total;
    /**
     * 成功次数
     */
    private String success;
    /**
     * 失败次数
     */
    private String fail;
    /**
     * 平均RT
     */
    private String rt;
    /**
     * 失败率
     */
    private String failRate;

}
