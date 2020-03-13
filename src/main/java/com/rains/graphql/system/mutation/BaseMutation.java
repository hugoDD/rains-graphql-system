package com.rains.graphql.system.mutation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.common.rsql.RsqlToMybatisPlusWrapper;
import com.rains.graphql.common.utils.BeanMapUtils;
import com.rains.graphql.system.service.IBaseService;
import com.rains.graphql.system.service.impl.BaseService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
@Transactional
public  class BaseMutation {


    public  <T> boolean baseMutation(QueryRequest<T> request, DataFetchingEnvironment env,IBaseService<T> service) {

        switch (request.getOpt()){
            case "insert":
                if(request.getBatchdatas() !=null && request.getBatchdatas().size()>0){
                  return   service.saveBatch(request.getBatchdatas() );
                }
                return service.save(request.getData());
            case "update":
                if(request.getBatchdatas() !=null && request.getBatchdatas().size()>0){
                    return   service.saveOrUpdateBatch(request.getBatchdatas() );
                }
                return service.saveOrUpdate(request);
            case "delete":
                return service.delete(request);
            case "export" :
                 service.export(request,env);
                 return true;

        }

       return false;
    }
 public  <T> boolean baseMutation(QueryRequest<T> request, DataFetchingEnvironment env,IBaseService<T> service, Consumer<QueryRequest<T>> consumer) {



        switch (request.getOpt()){
            case "insert":
                if(request.getBatchdatas() !=null && request.getBatchdatas().size()>0){
                  return   service.saveBatch(request.getBatchdatas() );
                }
                return service.save(request.getData());
            case "update":
                if(request.getBatchdatas() !=null && request.getBatchdatas().size()>0){
                    return   service.saveOrUpdateBatch(request.getBatchdatas() );
                }
                return service.saveOrUpdate(request);
            case "delete":
                return service.delete(request);
            case "export" :
                 service.export(request,env);
                 return true;
            default:
                consumer.accept(request);
                return true;
        }


    }


}
