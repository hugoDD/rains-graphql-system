package com.rains.graphql.system.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hugoDD
 */
@Slf4j
@Component
public class LoginLogQuery implements GraphQLQueryResolver {

    @Autowired
    private LoginLogService loginLogService;

    @RequiresPermissions("loginLog:view")
    public PageData<LoginLog> loginLogPages(QueryRequest request) {

        return loginLogService.query(request);
    }


}
