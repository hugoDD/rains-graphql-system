package com.rains.graphql.common.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.rains.graphql.common.utils.MD5Util;
import com.rains.graphql.common.utils.SysUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JWTUtilTest {

    @Test
    public void sign() {
        String username = StringUtils.lowerCase("admin");
        String secret = MD5Util.encrypt("admin", "123456");
        // Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withClaim("username", username)
                .sign(algorithm);
        System.out.println("pwd: "+secret);
        System.out.println("before token:"+token);
        String encryptToken = SysUtil.encryptToken(token);
        System.out.println("encryptToken: "+encryptToken);

       String decryptToken = SysUtil.decryptToken(encryptToken);
        Assert.assertEquals(token,decryptToken);

       String curusername = JWTUtil.getUsername(decryptToken);
       Assert.assertNotNull(curusername);
       System.out.println("username:"+curusername);
       Assert.assertEquals(username,curusername);


    }


    @Test
    public void testpwd(){
        String pwdExpress="^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*<>?:])[a-zA-Z0-9!@#$%^&*<>?:]{8,50}$";
        String strPwd="123Abc!@#";
        Pattern pattern = Pattern.compile(pwdExpress);
		Matcher matcher = pattern.matcher(strPwd);
        System.out.println(matcher.matches());
    }
}
