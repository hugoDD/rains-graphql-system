package com.rains.graphql.system.service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    IPage<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    Role findByName(String roleName);

    void createRole(Role role);
    @CacheInvalidate(name="permCache-")
    void deleteRoles(String[] roleIds) throws Exception;

    @CacheInvalidate(name="permCache-")
    void updateRole(Role role) throws Exception;
}
