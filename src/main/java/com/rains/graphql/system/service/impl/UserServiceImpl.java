package com.rains.graphql.system.service.impl;

import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.utils.MD5Util;
import com.rains.graphql.common.utils.SortUtil;
import com.rains.graphql.system.dao.UserMapper;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.domain.UserRole;
import com.rains.graphql.system.manager.UserManager;
import com.rains.graphql.system.service.UserConfigService;
import com.rains.graphql.system.service.UserRoleService;
import com.rains.graphql.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserManager userManager;


    @Override
    public User findByName(String username) {
       return  baseMapper.findDetail(username);
    }


    @Override
    public IPage<User> findUserDetail(User user, QueryRequest request) {
        try {
            Page<User> page = new Page<>();
            SortUtil.handlePageSort(request, page, "userId", RainsConstant.ORDER_ASC, false);
            return this.baseMapper.findUserDetail(page, user);
        } catch (Exception e) {
            log.error("查询用户异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLoginTime(String username) throws Exception {
        User user = new User();
        user.setLastLoginTime(new Date());

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));


    }

    @Override
    @Transactional
    public void createUser(User user) throws Exception {
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setPassword(MD5Util.encrypt(user.getUsername(), User.DEFAULT_PASSWORD));
        save(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));


    }

    @Override
    @Transactional
    public void updateUser(User user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setModifyTime(new Date());
        updateById(user);

        userRoleService.deleteUserRolesByUserId(new String[]{user.getUserId()+""});
        //userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));

        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) throws Exception {
        List<String> list = Arrays.asList(userIds);

        removeByIds(list);

        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        // 删除用户个性化配置
        this.userConfigService.deleteByUserId(userIds);
    }

    @Override
    @Transactional
    public void updateProfile(User user) throws Exception {
        updateById(user);

    }

    @Override
    @Transactional
    public void updateAvatar(String username, String avatar) throws Exception {
        User user = new User();
        user.setAvatar(avatar);

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));

    }

    @Override
    @Transactional
    public void updatePassword(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));

    }

    @Override
    @Transactional
    public void regist(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.save(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        userRoleService.save(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));


    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {

            User user = new User();
            user.setPassword(MD5Util.encrypt(username, User.DEFAULT_PASSWORD));

            this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        }

    }

    private void setUserRoles(User user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.userRoleService.save(ur);
        });
    }
}
