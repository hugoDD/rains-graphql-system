package com.rains.graphql.system.resolvers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rains.graphql.common.domain.router.RouterMeta;
import com.rains.graphql.common.domain.router.VueRouter;
import com.rains.graphql.common.utils.RouterUtils;
import com.rains.graphql.common.utils.StringUtils;
import com.rains.graphql.system.dao.LoginLogMapper;
import com.rains.graphql.system.domain.*;
import com.rains.graphql.system.manager.UserManager;
import com.rains.graphql.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
    private DeptService deptService;

    @Autowired
    private IUserPostService userPostService;

    @Autowired
    private IPostService postService;


    @Autowired
    private LoginLogMapper loginLogMapper;

    public User userDetail(User user) {
        return user;
    }

    public List<Role> getRoles(User user) {
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles = roleService.findUserRole(user.getUsername());
//        List<Long> roleIds=roles.stream().map(role ->role.getRoleId()).collect(Collectors.toList());
//        String roleIdStr= StringUtils.join(roleIds.toArray(new Long[roleIds.size()]),",");
//        user.setRoleId(roleIdStr);
        return roles;
    }

    public List<String> getRoleKeys(User user) {
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles = roleService.findUserRole(user.getUsername());
        List<String> roleNames = roles.stream().map(role -> role.getRoleKey()).collect(Collectors.toList());
//        String roleIdStr= StringUtils.join(roleIds.toArray(new Long[roleIds.size()]),",");
//        user.setRoleId(roleIdStr);
        return roleNames;
    }


    public List<VueRouter<Menu>> getRouters(User user) {
        if ("admin".equals(user.getUsername())) {
            List<VueRouter<Menu>> routes = new ArrayList<>();
            List<Menu> menus = menuService.list();
            menus = RouterUtils.getChildPerms(menus, 0);
            menus.stream().filter(m -> !"F".equals(m.getType())).filter(m -> "0".equals(m.getVisible())).forEach(menu -> {
                VueRouter<Menu> router = new VueRouter<>();
                router.initChildren();
                router.setId(menu.getMenuId().toString());
                router.setParentId(menu.getParentId().toString());
                router.setPath(RouterUtils.getRouterPath(menu));
                router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                router.setName(StringUtils.capitalize(menu.getPath()));
                router.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon(), true));
                List<Menu> cMenus = menu.getChildren();
                if (!cMenus.isEmpty() && cMenus.size() > 0 && "M".equals(menu.getType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                    router.setChildren(RouterUtils.buildMenus(cMenus));
                }

                routes.add(router);


            });
            //  List<VueRouter<Menu>>  routers =TreeUtil.buildVueRouter(routes);
            return routes;
        }

        return this.userManager.getUserRouters(user.getUsername());
    }

    public VisitData getVisitData(User user) {
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

    public List<Menu> getMenuList(User user) {
        return menuService.findUserMenus(user.getUsername());
    }

    public Set<String> getPermissions(User user) {
        if ("admin".equals(user.getUsername())) {
            Set<String> perms = new HashSet<>();
            perms.add("*:*:*");
            return perms;
        }
        List<Menu> permissionList = menuService.findUserPermissions(user.getUsername());
        return permissionList.stream().map(Menu::getPerms).flatMap(e -> Stream.of(e.split(","))).collect(Collectors.toSet());
    }

    public String getDeptName(User user) {
        Dept dept = deptService.getById(user.getDeptId());
        if (dept == null) {
            return "";
        }
        return dept.getDeptName();
    }

    public List<Long> getRoleIds(User user) {
        //修复用户修改自己的个人信息第二次提示roleId不能为空
        List<Role> roles = roleService.findUserRole(user.getUsername());
        List<Long> roleIds = roles.stream().map(role -> role.getRoleId()).collect(Collectors.toList());
//        user.setRoleId(roleIdStr);
        return roleIds;
    }

    public List<Long> getPostIds(User user) {
        LambdaQueryWrapper<UserPost> query = Wrappers.lambdaQuery();
        query.eq(UserPost::getUserId, user.getUserId());
        List<UserPost> userPosts = userPostService.list(query);
        List<Long> postIds = userPosts.stream().map(UserPost::getPostId).collect(Collectors.toList());
        return postIds;
    }

    public Collection<Post> getPosts(User user) {
        List<Long> postIds = getPostIds(user);
        if (postIds.isEmpty()) {
            Collection<Post> c = new HashSet();
            Post post = new Post();
            post.setPostId(-1L);
            post.setPostCode("0000");
            post.setPostName("默认岗位");
            c.add(post);
            return c;
        }
        LambdaQueryWrapper<Post> query = Wrappers.lambdaQuery();
        return postService.listByIds(postIds);
    }
}
