package com.rains.graphql.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rains.graphql.system.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findUserRole(String userName);

}
