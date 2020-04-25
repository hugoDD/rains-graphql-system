package com.rains.graphql.system.service;


import com.rains.graphql.system.domain.UserRole;

import java.util.List;

public interface UserRoleService extends IBaseService<UserRole> {

    void deleteUserRolesByRoleId(String[] roleIds);

    void deleteUserRolesByUserId(String[] userIds);

    List<String> findUserIdsByRoleId(String[] roleIds);
}
