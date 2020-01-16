package com.rains.graphql.system.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 获取系统访问记录
 */
@Getter
@Setter
public class VisitData implements Serializable {

    /**
     * 获取系统总访问次数
     */
    private long totalVisitCount;
    /**
     * 获取系统今日访问次数
     */
    private long todayVisitCount;
    /**
     * 获取系统今日访问 IP数
     */
    private long todayIp;
    /**
     * 获取系统近七天来的访问记录
     */
    private List<Map<String, Object>> lastSevenVisitCount;

    /**
     * 获取用户近七天来的访问记录
     */
    private List<Map<String, Object>> lastSevenUserVisitCount;

}
