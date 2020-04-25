package com.rains.graphql.common.domain;

import lombok.Data;

/**
 * 项目基本信息
 *
 * @author novel
 * @date 2019/12/5
 */
@Data
public class ProjectInfo {
    /**
     * 系统版本号
     */
    private String version = "0.0.1";

    /**
     * 项目名称
     */
    private String name = "novel";

    /**
     * 版权年份
     */
    private String copyrightYear = "2019";
    /**
     * 版权所属公司
     */
    private String copyrightCompany = "cnovel.club";
}
