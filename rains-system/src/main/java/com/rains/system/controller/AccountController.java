package com.rains.system.controller;


import com.rains.system.domain.User;
import com.rains.system.service.UserService;
import org.illyasviel.elide.spring.boot.jwt.JwtUtil;
import org.illyasviel.elide.spring.boot.response.Rets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AccountController
 *
 * @author hugoDD
 */
@RestController
@RequestMapping("/account")
public class AccountController {
     private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录<br>
     * 1，验证没有注册<br>
     * 2，验证密码错误<br>
     * 3，登录成功
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(@RequestParam("username") String userName,
                        @RequestParam("password") String password){
        try {
            logger.info("用户登录:" + userName + ",密码:" + password);
            //1,
            User user = userService.findByAccount(userName);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }
//            String passwdMd5 = MD5.md5(password, user.getSalt());
//            //2,
//            if (!user.getPassword().equals(passwdMd5)) {
//                return Rets.failure("输入的密码错误");
//            }


            String token = JwtUtil.sign(user.getId(),user.getName(),user.getPassword());
            Map<String, String> result = new HashMap<>(1);
            logger.info("token:{}",token);
            result.put("token", token);
            result.put("accessToken", token);

            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public Object logout(){

        return Rets.success();
    }



}
