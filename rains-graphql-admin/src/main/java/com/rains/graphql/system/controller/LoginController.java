package com.rains.graphql.system.controller;

import com.rains.graphql.common.authentication.JWTToken;
import com.rains.graphql.common.authentication.JWTUtil;
import com.rains.graphql.common.config.ProjectConfig;
import com.rains.graphql.common.domain.ProjectInfo;
import com.rains.graphql.common.domain.ResultResponse;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.properties.RainsGraphqlProperties;
import com.rains.graphql.common.utils.DateUtil;
import com.rains.graphql.common.utils.IPUtil;
import com.rains.graphql.common.utils.MD5Util;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.manager.UserManager;
import com.rains.graphql.system.service.LoginLogService;
import com.rains.graphql.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
public class LoginController {


    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private RainsGraphqlProperties properties;

    @Autowired
    private ProjectConfig projectConfig;


    @PostMapping("/login")
    public ResultResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);

        final String errorMessage = "用户名或密码错误";
        User user = this.userManager.getUser(username);

        if (user == null)
            throw new SysException(errorMessage);
        if (!StringUtils.equals(user.getPassword(), password)) {
            ResultResponse rs = new ResultResponse().message(errorMessage);
            rs.setCode(403);
            return rs;
        }
        if (User.STATUS_LOCK.equals(user.getStatus()))
            throw new SysException("账号已被锁定,请联系管理员！");

        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录


        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        String agent = request.getHeader("User-Agent");
        loginLog.setSystem(agent);
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        this.loginLogService.saveLoginLog(loginLog);

        String token = SysUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        response.setHeader("authorization", jwtToken.getToken());
        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new ResultResponse().message("认证成功").data(userInfo);
    }

    /**
     * 获取项目基本信息
     *
     * @return 项目基本信息
     */
    @GetMapping(value = "/getProjectInfo")
    public ProjectInfo getProjectInfo() {
        return projectConfig.getProjectInfo();
    }

    @PostMapping("/logout")
    public ResultResponse logout() {
        SecurityUtils.getSubject().logout();
        return new ResultResponse().message("退出成功！");
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        return userInfo;
    }
}
