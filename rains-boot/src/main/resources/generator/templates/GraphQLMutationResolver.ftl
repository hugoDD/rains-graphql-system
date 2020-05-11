package ${basePackage}.mutation;


import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${servicePackage}.I${className}Service;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.mutation.BaseMutation;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* ${tableComment} Mutation
*
* @author ${author}
* @date ${date}
*/
@Slf4j
@Component
public class ${className}Mutation implements GraphQLMutationResolver {
@Autowired
private BaseMutation mutation;

@Autowired
private I${className}Service ${className?uncap_first}Service;

@Log("[#request.opt]操作系统日志")
@RequiresPermissions("${className?uncap_first}:[#request.opt]")
public boolean ${className?uncap_first}BaseMutation(QueryRequest request, ${className} entity, DataFetchingEnvironment env) {
request.setData(entity);
return mutation.baseMutation(request, env,${className?uncap_first}Service);
}
}
