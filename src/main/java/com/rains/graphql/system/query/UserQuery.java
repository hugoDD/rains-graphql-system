package com.rains.graphql.system.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.utils.MD5Util;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.service.RoleService;
import com.rains.graphql.system.service.UserConfigService;
import com.rains.graphql.system.service.UserService;
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

    @RequiresPermissions("user:view")
    public PageData<User> userPages(QueryRequest request) {
        return userService.query(request);
    }

    public User detail(String username) {
        String currentUserName = username;
        if (StringUtils.isEmpty(username)) {
            currentUserName = SysUtil.getCurrentUserName();
        }
        User user = this.userService.findByName(currentUserName);

        return user;
    }

    @RequiresPermissions("user:view")
    public PageData<User> userPage(QueryRequest page, User user) {
        IPage<User> users = userService.findUserDetail(user, page);
        return new PageData<User>(users.getTotal(), users.getRecords());
    }


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

}
