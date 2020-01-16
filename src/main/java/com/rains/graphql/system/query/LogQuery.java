package com.rains.graphql.system.query;

import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Log;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.LogService;
import com.rains.graphql.system.service.LoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Validated
@Component
public class LogQuery implements GraphQLQueryResolver {


    @Autowired
    private LogService logService;

    @GetMapping
    @RequiresPermissions("log:view")
    public PageData<Log> logPage(QueryRequest request, Log log) {
         logService.query(request);
        IPage<Log> page =logService.findLogs(request, log);
        return new PageData<>(page.getTotal(),page.getRecords());
    }
    @RequiresPermissions("log:detail")
    public Log logDetail(Log log){
        return logService.getById(log.getId());
    }

    @Autowired
    private LoginLogService loginLogService;

    public PageData<LoginLog> loginLogPage(QueryRequest request, LoginLog loginLog) {
        IPage<LoginLog> page= this.loginLogService.findLoginLogs(loginLog, request);
        return new PageData<>(page.getTotal(),page.getRecords());
    }


}
