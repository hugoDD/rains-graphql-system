package com.rains.graphql.system.resolvers;

import com.rains.graphql.common.domain.router.VueRouter;
import com.rains.graphql.system.dao.LoginLogMapper;
import com.rains.graphql.system.domain.Menu;
import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.domain.VisitData;
import com.rains.graphql.system.manager.UserManager;
import com.rains.graphql.system.service.MenuService;
import com.rains.graphql.system.service.RoleService;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class UserResolvers implements GraphQLResolver<User> {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private MenuService menuService;



    @Autowired
    private LoginLogMapper loginLogMapper;

    public User userDetail(User user){
        return user;
    }

    public List<Role> getRoles(User user){
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles=roleService.findUserRole(user.getUsername());
//        List<Long> roleIds=roles.stream().map(role ->role.getRoleId()).collect(Collectors.toList());
//        String roleIdStr= StringUtils.join(roleIds.toArray(new Long[roleIds.size()]),",");
//        user.setRoleId(roleIdStr);
        return roles;
    }


    public List<VueRouter<Menu>> getRouters(User user) {
        return this.userManager.getUserRouters(user.getUsername());
    }

    public VisitData getVisitData(User user){
        VisitData visitData = new VisitData();
        // 获取系统访问记录
        Long totalVisitCount = loginLogMapper.findTotalVisitCount();
        visitData.setTotalVisitCount(totalVisitCount);
        Long todayVisitCount = loginLogMapper.findTodayVisitCount();
        visitData.setTotalVisitCount(totalVisitCount);
        Long todayIp = loginLogMapper.findTodayIp();
        visitData.setTodayIp(todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastTenDaysVisitCount(null);
       visitData.setLastSevenVisitCount(lastSevenVisitCount);

        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastTenDaysVisitCount(user);
        visitData.setLastSevenUserVisitCount(lastSevenUserVisitCount);


        return visitData;
    }

    public List<Menu> getMenuList(User user){
        return menuService.findUserMenus(user.getUsername());
    }

    public Set<String> getPermissions(User user){
        List<Menu> permissionList= menuService.findUserPermissions(user.getUsername());
        return permissionList.stream().map(Menu::getPerms).flatMap(e-> Stream.of(e.split(","))).collect(Collectors.toSet());
    }

}
