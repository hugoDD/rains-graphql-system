package ${basePackage}.${servicePackage};

import ${basePackage}.${entityPackage}.${className};

import com.rains.graphql.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rains.graphql.system.service.IBaseService;


import java.util.List;

/**
* ${tableComment} Service接口
*
* @author ${author}
* @date ${date}
*/
public interface I${className}Service extends IBaseService<${className}> {

/**
* 查询（所有）
*
* @param ${className?uncap_first} ${className?uncap_first}
* @return List<${className}>
*/
List<${className}> find${className}s(${className} ${className?uncap_first});


}
