package com.rains.graphql.system.service;

import com.alicp.jetcache.anno.*;
import com.rains.graphql.system.domain.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService extends IBaseService<Menu> {
    @Cached(name = "permCache-", key = "#username", expire = 3600, cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600)
    @CachePenetrationProtect
    List<Menu> findUserPermissions(String username);

    List<Menu> findUserMenus(String username);


    Map<String, Object> findMenus(Menu menu);

    List<Menu> findMenuList(Menu menu);

    void createMenu(Menu menu);

    @CacheInvalidate(name = "permCache-")
    void updateMenu(Menu menu) throws Exception;

    /**
     * 递归删除菜单/按钮
     *
     * @param menuIds menuIds
     */
    @CacheInvalidate(name = "permCache-")
    void deleteMeuns(String[] menuIds) throws Exception;

}
