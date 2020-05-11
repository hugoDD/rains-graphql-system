package com.rains.graphql.system.resolvers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.system.domain.*;
import com.rains.graphql.system.service.*;
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
    private MenuService menuService;

    @Autowired
    private RoleMenuServie roleMenuServie;

    @Autowired
    private IRoleDeptService roleDeptService;

    @Autowired
    private DeptService deptService;

    public List<String> getMenuIds(Role role) {
        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(role.getRoleId() + "");
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    public List<Menu> getMenus(Role role) {

        try {
            SysUtil.getSubject().checkRole("admin");
            return menuService.list();
        } catch (Exception e) {
            log.info("It's not admin role");
        }

        LambdaQueryWrapper<Menu> query = Wrappers.lambdaQuery();
        query.inSql(Menu::getMenuId, "select menu_id from t_role_menu where role_id = " + role.getRoleId());
        List<Menu> menus = menuService.list(query);
        return menus;
    }

    public List<Long> getDeptIds(Role role) {
        LambdaQueryWrapper<RoleDept> query = Wrappers.lambdaQuery();
        query.eq(RoleDept::getRoleId, role.getRoleId());
        List<RoleDept> roleDepts = roleDeptService.list(query);
        List<Long> deptIds = roleDepts.stream().map(RoleDept::getDeptId).collect(Collectors.toList());
        return deptIds;
    }

    public List<Dept> getDepts(Role role) {
        List<Dept> depts = deptService.list();
        return depts;
    }
}
