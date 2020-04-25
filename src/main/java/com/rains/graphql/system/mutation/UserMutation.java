package com.rains.graphql.system.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.domain.UserConfig;
import com.rains.graphql.system.service.RoleService;
import com.rains.graphql.system.service.UserConfigService;
import com.rains.graphql.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class UserMutation implements GraphQLMutationResolver {
    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private RoleService roleService;


    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("user:[#request.opt]")
    public boolean userBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        if ("importTable".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> userService.export(q, env);
            request.opt("importTable", exportOpt);
        }


        return userService.baseOpt(request);
    }

    @Log("新增用户")
    // @PostMapping
    @RequiresPermissions("user:add")
    public User addUser(@Valid User user) throws SysException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new SysException(message);
        }

        return user;
    }

    @Log("修改用户")
    //@PutMapping
    @RequiresPermissions("user:update")
    public User updateUser(@Valid User user) throws SysException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return user;
    }

    @Log("删除用户")
    // @DeleteMapping("/{userIds}")
    @RequiresPermissions("user:delete")
    public boolean deleteUsers(@NotBlank(message = "{required}") String[] userIds) throws SysException {
        try {
            //String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(userIds);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    // @PutMapping("profile")
    public User updateProfile(@Valid User user) throws SysException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new SysException(message);

        }
        return user;
    }

    //@PutMapping("avatar")
    public Boolean updateAvatar(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String avatar) throws SysException {
        try {
            this.userService.updateAvatar(username, avatar);
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    // @PutMapping("userconfig")
    public UserConfig updateUserConfig(@Valid UserConfig userConfig) throws SysException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new SysException(message);
        }

        return userConfig;
    }


    // @PutMapping("password")
    public boolean updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws SysException {
        try {
            userService.updatePassword(username, password);
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    // @PutMapping("password/reset")
    @RequiresPermissions("user:reset")
    public boolean resetPassword(@NotBlank(message = "{required}") String[] usernames) throws SysException {
        try {
            //String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernames);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new SysException(message);
        }

        return true;
    }

    @RequiresPermissions("user:export")
    public void userExport(QueryRequest queryRequest, User user, DataFetchingEnvironment env) throws SysException {
        try {
            List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(User.class, GraphQLHttpUtil.getResponse(env)).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new SysException(message);
        }
    }

}
