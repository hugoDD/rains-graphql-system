package com.rains.graphql.system.service;

import com.alicp.jetcache.anno.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rains.graphql.system.domain.UserConfig;

public interface UserConfigService extends IService<UserConfig> {

    /**
     * 通过用户 ID 获取前端系统个性化配置
     *
     * @param userId 用户 ID
     * @return 前端系统个性化配置
     */
    @Cached(name = "userConfigCache-", key = "#userId", expire = 3600, cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600)
    @CachePenetrationProtect
    UserConfig findByUserId(String userId);

    /**
     * 生成用户默认个性化配置
     *
     * @param userId 用户 ID
     */
    void initDefaultUserConfig(String userId);

    /**
     * 通过用户 ID 删除个性化配置
     *
     * @param userIds 用户 ID 数组
     */
    @CacheInvalidate(name = "userConfigCache-", key = "#userIds", multi = true)
    void deleteByUserId(String... userIds);

    /**
     * 更新用户个性化配置
     *
     * @param userConfig 用户个性化配置
     */
    @CacheUpdate(name = "userConfigCache-", key = "#userConfig.userId", value = "#userConfig")
    void update(UserConfig userConfig) throws Exception;
}
