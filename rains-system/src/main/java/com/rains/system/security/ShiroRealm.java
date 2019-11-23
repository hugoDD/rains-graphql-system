package com.rains.system.security;


import com.rains.system.domain.Role;
import com.rains.system.domain.User;
import com.rains.system.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.illyasviel.elide.spring.boot.jwt.JwtToken;
import org.illyasviel.elide.spring.boot.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
public class ShiroRealm extends AuthorizingRealm {


    @Autowired
    private AccountRepository userManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**`
     * 授权模块，获取用户角色和权限
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        Long userId = JwtUtil.getUserId(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        User user = userManager.getOne(userId);

        Set<String> roleSet=user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        simpleAuthorizationInfo.setStringPermissions(user.getPermissions());
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();

        // 从 redis里获取这个 token
//        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
//        String ip = IPUtil.getIpAddr(request);
//
//        String encryptToken = FebsUtil.encryptToken(token);
//        String encryptTokenInRedis = null;
//        try {
//            encryptTokenInRedis = redisService.get(FebsConstant.TOKEN_CACHE_PREFIX + encryptToken + "." + ip);
//        } catch (Exception ignore) {
//        }
//        // 如果找不到，说明已经失效
//        if (StringUtils.isBlank(encryptTokenInRedis))
//            throw new AuthenticationException("token已经过期");
        Long userId = JwtUtil.getUserId(token);
        if (Objects.isNull(userId))
            throw new AuthenticationException("token校验不通过");

        // 通过用户名查询用户信息
        User user = userManager.getOne(userId);

        if (user == null)
            throw new AuthenticationException("用户名或密码错误");
        if (!JwtUtil.verify(token, userId, user.getPassword()))
            throw new AuthenticationException("token校验不通过");
        return new SimpleAuthenticationInfo(token, token, "graphql_shiro_realm");
    }
}
