package com.rains.graphql.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.rains.graphql.common.utils.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

@Slf4j
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("updateTime", new Date(), metaObject);
        String curUserName = "none";
        try {
            curUserName = SysUtil.getCurrentUserName();
        } catch (RuntimeException e) {
            log.warn("get current user name fail");
        }
        setFieldValByName("createBy", curUserName, metaObject);
        setFieldValByName("updateBy", curUserName, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        String curUserName = "none";
        try {
            curUserName = SysUtil.getCurrentUserName();
        } catch (RuntimeException e) {
            log.warn("get current user name fail");
        }
        setFieldValByName("updateBy", curUserName, metaObject);
    }
}

