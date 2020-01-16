package com.rains.graphql.system.resolvers;

import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.domain.RoleMenu;
import com.rains.graphql.system.service.RoleMenuServie;
import com.rains.graphql.system.service.RoleService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RoleResolvers implements GraphQLResolver<Role> {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuServie roleMenuServie;

    public List<String> getMenuIds(Role role) {
        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(role.getRoleId()+"");
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

}
