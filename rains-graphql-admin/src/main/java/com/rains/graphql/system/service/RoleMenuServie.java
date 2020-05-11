package com.rains.graphql.system.service;

import com.rains.graphql.system.domain.RoleMenu;

import java.util.List;

public interface RoleMenuServie extends IBaseService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
