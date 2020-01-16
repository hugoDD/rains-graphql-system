package com.rains.graphql.system.query;


import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.LoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hugoDD
 */
@Slf4j
@Component
public class LoginLogQuery  implements GraphQLQueryResolver {

    @Autowired
    private LoginLogService loginLogService;

    public PageData<LoginLog> loginLogList(LoginLog loginLog, QueryRequest request) {
        IPage<LoginLog> page= this.loginLogService.findLoginLogs(loginLog, request);
        return new PageData<>(page.getTotal(),page.getRecords());
    }





}
