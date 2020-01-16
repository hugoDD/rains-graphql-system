package com.rains.graphql.system.query;


import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IBaseService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;



@Component
public  class BaseQuery{
//    public  <T> boolean baseQuery(QueryRequest<T> request, DataFetchingEnvironment env, IBaseService<T> service) {
//
//        switch (request.getOpt()){
//            case "insert":
//                return service.save(request.getData());
//            case "update":
//                return service.saveOrUpdatee(request);
//            case "delete":
//                return service.delete(request);
//            case "export" :
//                service.export(request,env);
//                return true;
//        }
//
//        return false;
//    }

}
