package com.rains.graphql.system.query;

import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.common.utils.MD5Util;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.service.RoleService;
import com.rains.graphql.system.service.UserConfigService;
import com.rains.graphql.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Slf4j
@Component
public class UserQuery implements GraphQLQueryResolver {

    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private RoleService roleService;

    //@GetMapping("check/{username}")
//    public boolean checkUserName(String username) {
//        return this.userService.findByName(username) == null;
//    }

   // @GetMapping("/{username}")
    public User detail(String username) {
        String currentUserName = username;
        if(StringUtils.isEmpty(username)){
            currentUserName=  SysUtil.getCurrentUserName();
        }
        User user=this.userService.findByName(currentUserName);
        //修复用户修改自己的个人信息第二次提示roleId不能为空
//        List<Role> roles=roleService.findUserRole(username);
//        List<Long> roleIds=roles.stream().map(role ->role.getRoleId()).collect(Collectors.toList());
//        String roleIdStr=StringUtils.join(roleIds.toArray(new Long[roleIds.size()]),",");
//        user.setRoleId(roleIdStr);
        return user;
    }

    //@GetMapping
    @RequiresPermissions("user:view")
    public PageData<User> userPage(QueryRequest page, User user) {
       IPage<User> users = userService.findUserDetail(user, page);
       return new PageData<User>(users.getTotal(),users.getRecords());
       // return getDataTable(userService.findUserDetail(user, queryRequest));
    }



    //@GetMapping("password/check")
    public boolean checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        String encryptPassword = MD5Util.encrypt(username, password);
        User user = userService.findByName(username);
        if (user != null)
            return StringUtils.equals(user.getPassword(), encryptPassword);
        else
            return false;
    }



//    @PostMapping("excel")
//    @RequiresPermissions("user:export")
//    public void export(QueryRequest queryRequest, User user, HttpServletResponse response) throws FebsException {
//        try {
//            List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
//            ExcelKit.$Export(User.class, response).downXlsx(users, false);
//        } catch (Exception e) {
//            message = "导出Excel失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
}
