package com.rains.graphql.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.log.ILogService;
import com.rains.graphql.system.domain.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;


public interface LogService extends ILogService, IBaseService<Log> {

    IPage<Log> findLogs(QueryRequest request, Log log);

    void deleteLogs(String[] logIds);

    @Async
    void saveLog(ProceedingJoinPoint point, Log log) throws JsonProcessingException;
}
