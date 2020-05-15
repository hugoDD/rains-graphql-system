package com.rains.graphql.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Spring Context 工具类
 *
 * @author MrBird
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 获取resouce中文件
     *
     * @return
     * @throws IOException
     */
    public static byte[] getResourceBytes(String resourcePath) throws IOException {
        byte[] result = null;
        ClassPathResource classPathResource = new ClassPathResource(resourcePath);
        InputStream arthasInputStream = classPathResource.getInputStream();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = arthasInputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        arthasInputStream.close();
        arthasInputStream = null;
        result = swapStream.toByteArray();
        swapStream.close();
        swapStream = null;
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

}
