package com.rains.graphql.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MD5Util {

    private static final String ALGORITH_NAME = "md5";
    private static final int HASH_ITERATIONS = 2;

    protected MD5Util() {

    }

    public static String encrypt(String password) {
        return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(password), HASH_ITERATIONS).toHex();
    }

    public static String encrypt(String username, String password) {
        return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(username.toLowerCase() + password),
                HASH_ITERATIONS).toHex();
    }

    public static String hash(String s) {
        try {
            return new String(encrypt(s).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    public static void main(String[] args) {
        System.out.println(encrypt("dou", "admin"));
    }

}
