package com.rains.graphql.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

public interface ILogService {
    @Async
    void saveLog(ProceedingJoinPoint point, LogVo log) throws JsonProcessingException;
}
