package com.rains.graphql.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.rains.graphql.common.authentication.JWTUtil;
import com.rains.graphql.common.domain.RainsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.IntStream;

/**
 * 工具类
 */
@Slf4j
public class SysUtil {


    /**
     * 获取当前操作用户
     *
     * @return 用户信息
     */
//    public static User getCurrentUser() {
//        String token = (String) SecurityUtils.getSubject().getPrincipal();
//        String username = JWTUtil.getUsername(token);
//        UserService userService = SpringContextUtil.getBean(UserService.class);
//
//        return userService.findByName(username);
//    }

    /**
     * 获取当前操作用户
     *
     * @return 用户信息
     */
    public static String getCurrentUserName() {
        if (SecurityUtils.getSubject() == null) {
            return "none";
        }
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        if (com.rains.graphql.common.utils.StringUtils.isEmpty(token)) {
            return "none";
        }
        String username = JWTUtil.getUsername(token);

        return username;
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * token 加密
     *
     * @param token token
     * @return 加密后的 token
     */
    public static String encryptToken(String token) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(RainsConstant.TOKEN_CACHE_PREFIX);
            return encryptUtil.encrypt(token);
        } catch (Exception e) {
            log.info("token加密失败：", e);
            return null;
        }
    }

    /**
     * token 解密
     *
     * @param encryptToken 加密后的 token
     * @return 解密后的 token
     */
    public static String decryptToken(String encryptToken) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(RainsConstant.TOKEN_CACHE_PREFIX);
            return encryptUtil.decrypt(encryptToken);
        } catch (Exception e) {
            log.info("token解密失败：", e);
            return null;
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value))
            return value;
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0)
            return value;
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1)
                result.append(arr[i]).append(StringPool.UNDERSCORE);
            else
                result.append(arr[i]);
        });
        return StringUtils.lowerCase(result.toString());
    }

    /**
     * 字符串第一个字母大写
     *
     * @param str 被处理的字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(final String str) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.capitalize(str);
    }

    public static String underlineToCamel(final String str) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.underlineToCamel(str);
    }

    public static String underlineToCamelCapitalize(final String str) {
        String param = com.baomidou.mybatisplus.core.toolkit.StringUtils.underlineToCamel(str);
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.capitalize(param);
    }

    public static Class<?> getActualTypeArgument(Class clazz) {
        Class<?> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<?>) actualTypeArguments[0];
            }
        }

        return entitiClass;
    }

}
