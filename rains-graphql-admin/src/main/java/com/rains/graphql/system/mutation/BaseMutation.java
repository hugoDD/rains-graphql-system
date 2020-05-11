package com.rains.graphql.system.mutation;

import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.service.IBaseService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Slf4j
@Service
@Transactional
public class BaseMutation {


    public <T> boolean baseMutation(QueryRequest<T> request, DataFetchingEnvironment env, IBaseService<T> service) {

        switch (request.getOpt()) {
            case "insert":
                if (request.getBatchdatas() != null && request.getBatchdatas().size() > 0) {
                    return service.saveBatch(request.getBatchdatas());
                }
                return service.save(request.getData());
            case "update":
                if (request.getBatchdatas() != null && request.getBatchdatas().size() > 0) {
                    return service.saveOrUpdateBatch(request.getBatchdatas());
                }
                return service.saveOrUpdate(request);
            case "delete":
                return service.delete(request);
            case "export":
                service.export(request, env);
                return true;

        }

        return false;
    }

    public <T> boolean baseMutation(QueryRequest<T> request, DataFetchingEnvironment env, IBaseService<T> service, Consumer<QueryRequest<T>> consumer) {


        switch (request.getOpt()) {
            case "insert":
                if (request.getBatchdatas() != null && request.getBatchdatas().size() > 0) {
                    return service.saveBatch(request.getBatchdatas());
                }
                return service.save(request.getData());
            case "update":
                if (request.getBatchdatas() != null && request.getBatchdatas().size() > 0) {
                    return service.saveOrUpdateBatch(request.getBatchdatas());
                }
                return service.saveOrUpdate(request);
            case "delete":
                return service.delete(request);
            case "export":
                service.export(request, env);
                return true;
            default:
                consumer.accept(request);
                return true;
        }


    }


}
