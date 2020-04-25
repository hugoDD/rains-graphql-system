package com.rains.graphql.common.config;

import com.rains.graphql.common.domain.ProjectInfo;
import lombok.Data;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 项目信息配置
 *
 * @author novel
 * @date 2019/5/24
 */
@Data
@Component
@ConfigurationProperties(prefix = ProjectConfig.PROJECT_PREFIX)
public class ProjectConfig {
    public static final String PROJECT_PREFIX = "project";
    /**
     * 文件保存路径
     */
    @NotNull
    private static String profile = "/resources/";
    /**
     * 实例演示开关
     */
    private static boolean demoEnabled;
    /**
     * 获取地址ip开关
     */
    private static boolean addressEnabled;
    /**
     * 系统详细信息
     */
    @NestedConfigurationProperty
    private ProjectInfo projectInfo = new ProjectInfo();

    public static String getProfile() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return profile;
        } else if (SystemUtils.IS_OS_LINUX) {
            //如果是linux，那么去除windows下的盘符前缀
            return profile.replaceAll("^([a-zA-Z]):", "");
        } else {
            return profile;
        }
    }

    public void setProfile(String profile) {
        ProjectConfig.profile = profile;
    }

    public static boolean getDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        ProjectConfig.demoEnabled = demoEnabled;
    }

    public static boolean getAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        ProjectConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    public boolean isDemoEnabled() {
        return demoEnabled;
    }

    public boolean isAddressEnabled() {
        return addressEnabled;
    }

    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }
}
