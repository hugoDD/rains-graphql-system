package com.rains.graphql.system.mutation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.common.rsql.RsqlToMybatisPlusWrapper;
import com.rains.graphql.system.service.IBaseService;
import com.rains.graphql.system.service.impl.BaseService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public  class BaseMutation {


    public  <T> boolean baseMutation(QueryRequest<T> request, DataFetchingEnvironment env,IBaseService<T> service) {

        switch (request.getOpt()){
            case "insert":
                return service.save(request.getData());
            case "update":
                return service.saveOrUpdate(request);
            case "delete":
                return service.delete(request);
            case "export" :
                 service.export(request,env);
                 return true;
        }

       return false;
    }


}
