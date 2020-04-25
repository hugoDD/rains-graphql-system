package ${basePackage}.query;


import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${servicePackage}.I${className}Service;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
* ${tableComment} QueryResolver
*
* @author ${author}
* @date ${date}
*/
@Slf4j
@Component
public class ${className}Query implements GraphQLQueryResolver {

@Autowired
private I${className}Service ${className?uncap_first}Service;

@RequiresPermissions("${className?uncap_first}:view")
public PageData<${className}> ${className?uncap_first}Page(QueryRequest request) {
return ${className?uncap_first}Service.query(request);
}


}
