package com.rains.graphql.system.mutation;

import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.generator.GeneratorConstant;
import com.rains.graphql.common.properties.RainsGraphqlProperties;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.common.utils.FileUtil;
import com.rains.graphql.system.domain.Column;
import com.rains.graphql.system.domain.GeneratorConfig;
import com.rains.graphql.system.service.GeneratorConfigService;
import com.rains.graphql.system.service.GeneratorService;
import com.rains.graphql.system.service.impl.GeneratorHelper;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Slf4j
@Component
public class GeneratorMutation implements GraphQLMutationResolver {
    @Autowired
    private GeneratorConfigService generatorConfigService;

    private static final String SUFFIX = "_code.zip";

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private GeneratorHelper generatorHelper;

    @Autowired
    private DataSourceProperties properties;


    @RequiresPermissions("gen:config:update")
    public GeneratorConfig updateGeneratorConfig(GeneratorConfig generatorConfig) throws SysException {
        if (generatorConfig.getId()==null){
            throw new SysException("配置id不能为空");
        }
        this.generatorConfigService.updateGeneratorConfig(generatorConfig);
        return generatorConfig;
    }

    @RequiresPermissions("gen:generate:gen")
    public void generate( String database,String name, String remark, DataFetchingEnvironment env) throws Exception {
        GraphQLServletContext context = env.getContext();
        HttpServletResponse response = context.getHttpServletResponse();
       // properties.getSchema().

        GeneratorConfig generatorConfig = generatorConfigService.findGeneratorConfig();
        if (generatorConfig == null) {
            throw new SysException("代码生成配置为空");
        }

        String className = name;
        if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
            className = RegExUtils.replaceFirst(name, generatorConfig.getTrimValue(), StringUtils.EMPTY);
        }

        generatorConfig.setTableName(name);
        generatorConfig.setClassName(SysUtil.camelToUnderscore(className));
        generatorConfig.setTableComment(remark);
        // 生成代码到临时目录
        List<Column> columns = generatorService.getColumns(GeneratorConstant.DATABASE_TYPE, database, name);
        generatorHelper.generateEntityFile(columns, generatorConfig);
        generatorHelper.generateMapperFile(columns, generatorConfig);
        generatorHelper.generateMapperXmlFile(columns, generatorConfig);
        generatorHelper.generateServiceFile(columns, generatorConfig);
        generatorHelper.generateServiceImplFile(columns, generatorConfig);
        generatorHelper.generateControllerFile(columns, generatorConfig);
        // 打包
        String zipFile = System.currentTimeMillis() + SUFFIX;
        FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
        // 下载
        FileUtil.download(zipFile, name + SUFFIX, true, response);
        // 删除临时目录
        FileUtil.delete(GeneratorConstant.TEMP_PATH);
    }
}
