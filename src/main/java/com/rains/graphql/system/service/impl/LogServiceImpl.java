package com.rains.graphql.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.rsql.RsqlToMybatisPlusWrapper;
import com.rains.graphql.common.utils.AddressUtil;
import com.rains.graphql.common.utils.SortUtil;
import com.rains.graphql.system.dao.LogMapper;
import com.rains.graphql.system.domain.Log;
import com.rains.graphql.system.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends BaseService<LogMapper, Log> implements LogService {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public IPage<Log> findLogs(QueryRequest request, Log log) {
        try {
            if(request == null){
                request = new QueryRequest();
            }
            if(log ==null){
                log = new Log();
            }
            Wrapper<Log> queryWrapper= RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), Log.class);
//            QueryWrapper<Log> queryWrapper1 = new QueryWrapper<>();
//
//            if (StringUtils.isNotBlank(log.getUsername())) {
//                queryWrapper1.lambda().eq(Log::getUsername, log.getUsername().toLowerCase());
//            }
//            if (StringUtils.isNotBlank(log.getOperation())) {
//                queryWrapper.lambda().like(Log::getOperation, log.getOperation());
//            }
//            if (StringUtils.isNotBlank(log.getLocation())) {
//                queryWrapper.lambda().like(Log::getLocation, log.getLocation());
//            }
//            if (StringUtils.isNotBlank(log.getCreateTimeFrom()) && StringUtils.isNotBlank(log.getCreateTimeTo())) {
//                queryWrapper.lambda()
//                        .ge(Log::getCreateTime, log.getCreateTimeFrom())
//                        .le(Log::getCreateTime, log.getCreateTimeTo());
//            }

            Page<Log> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "createTime", RainsConstant.ORDER_DESC, true);

            return this.page(page, queryWrapper);
        } catch (Exception e) {
            LogServiceImpl.log.error("获取系统日志失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void saveLog(ProceedingJoinPoint joinPoint, Log log) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.rains.graphql.common.annotation.Log logAnnotation = method.getAnnotation(com.rains.graphql.common.annotation.Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setOperation(logAnnotation.value());
        }
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            log.setParams(params.toString());
        }
        log.setCreateTime(new Date());
        log.setLocation(AddressUtil.getCityInfo(log.getIp()));
        // 保存系统日志
        save(log);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
}


