package com.rains.graphql.system.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.LoginLog;
import com.rains.graphql.system.service.LogService;
import com.rains.graphql.system.service.LoginLogService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Slf4j
@Validated
@Component
public class LogMutation implements GraphQLMutationResolver {

    @Autowired
    private LogService logService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private BaseMutation mutation;

    @Log("操作系统日志")
    @RequiresPermissions("log:[#request.opt]")
    public boolean logBaseMutation(QueryRequest request,com.rains.graphql.system.domain.Log lg, DataFetchingEnvironment env) {
        request.setData(lg);
        return mutation.baseMutation(request, env,logService);
    }


    @Log("删除系统日志")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("log:delete")
    public boolean deleteLogs(String[] ids) throws SysException {
        try {
            // String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(ids);
        } catch (Exception e) {
            String message = "删除日志失败";
            log.error(message, e);
            throw new SysException(message);
        }

        return true;
    }

    @RequiresPermissions("loginlog:delete")
    @Log("删除登录日志")
    public Boolean deleteLoginLogs(String[] ids) {

        this.loginLogService.deleteLoginLogs(ids);
        return true;
    }

    @RequiresPermissions("log:export")
    public void exportLogs(QueryRequest request, com.rains.graphql.system.domain.Log lg, DataFetchingEnvironment env) {
        List<com.rains.graphql.system.domain.Log> logs = this.logService.findLogs(request, lg).getRecords();
        ExcelKit.$Export(com.rains.graphql.system.domain.Log.class, GraphQLHttpUtil.getResponse(env)).downXlsx(logs, false);
    }

    @RequiresPermissions("loginlog:export")
    public void exportLoginLogs(QueryRequest request, LoginLog loginLog, DataFetchingEnvironment env) {
        List<LoginLog> loginLogs = this.loginLogService.findLoginLogs(loginLog, request).getRecords();
        ExcelKit.$Export(LoginLog.class, GraphQLHttpUtil.getResponse(env)).downXlsx(loginLogs, false);
    }


}
