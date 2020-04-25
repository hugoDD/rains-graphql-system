package com.rains.graphql.system.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Log;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.LogService;
import com.rains.graphql.system.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Component
public class LogQuery implements GraphQLQueryResolver {


    @Autowired
    private LogService logService;
    @Autowired
    private LoginLogService loginLogService;

    @RequiresPermissions("log:view")
    public PageData<Log> logPage(QueryRequest request) {
        return logService.query(request);
    }

    @RequiresPermissions("log:detail")
    public Log logDetail(Log log) {
        return logService.getById(log.getId());
    }

    public PageData<LoginLog> loginLogPage(QueryRequest request) {
        return loginLogService.query(request);
    }


}
