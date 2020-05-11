package com.rains.graphql.common.runner;


import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


/**
 * 缓存初始化
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {

    @CreateCache(expire = 100, cacheType = CacheType.REMOTE, localLimit = 50)
    private Cache<String, String> testCache;


    @Autowired
    private ConfigurableApplicationContext context;


    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Redis连接中 ······");

            testCache.put("test", "test");
            if ("test".equals(testCache.get("test"))) {
                log.info("Redis连接成功 ······");
            } else {
                log.error("Redis连接失败              ");
            }


        } catch (Exception e) {
            log.error(" ____   __    _   _ ");
            log.error("                        ");
            log.error("Redis连接失败              ");
            context.close();
        }
    }
}
