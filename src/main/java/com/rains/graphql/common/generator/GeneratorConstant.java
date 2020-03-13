package com.rains.graphql.common.generator;

/**
 * 代码生成常量
 *
 * @author MrBird
 */
public class GeneratorConstant {

    /**
     * 数据库类型
     */
    public static final String DATABASE_TYPE = "mysql";
    /**
     * 数据库名称
     */
    public static final String DATABASE_NAME = "febs_cloud_base";

    /**
     * 生成代码的临时目录
     */
    public static final String TEMP_PATH = "febs_gen_temp/";

    /**
     * java类型文件后缀
     */
    public static final String JAVA_FILE_SUFFIX = ".java";
    /**
     * mapper文件类型后缀
     */
    public static final String MAPPER_FILE_SUFFIX = "Mapper.java";
    /**
     * service文件类型后缀
     */
    public static final String SERVICE_FILE_SUFFIX = "Service.java";
    /**
     * service impl文件类型后缀
     */
    public static final String SERVICEIMPL_FILE_SUFFIX = "ServiceImpl.java";
    /**
     * controller文件类型后缀
     */
    public static final String CONTROLLER_FILE_SUFFIX = "Controller.java";
    public static final String GRAPHQLQUERY_FILE_SUFFIX = "Query.java";
    public static final String GRAPHQLMUTATION_FILE_SUFFIX = "Mutation.java";
    public static final String GRAPHQL_FILE_SUFFIX = ".graphqls";
    /**
     * mapper xml文件类型后缀
     */
    public static final String MAPPERXML_FILE_SUFFIX = "Mapper.xml";
    /**
     * entity模板
     */
    public static final String ENTITY_TEMPLATE = "entity.ftl";
    /**
     * mapper模板
     */
    public static final String MAPPER_TEMPLATE = "mapper.ftl";
    /**
     * service接口模板
     */
    public static final String SERVICE_TEMPLATE = "service.ftl";
    /**
     * service impl接口模板
     */
    public static final String SERVICEIMPL_TEMPLATE = "serviceImpl.ftl";
    /**
     * controller接口模板
     */
    public static final String CONTROLLER_TEMPLATE = "controller.ftl";
    public static final String GRAPHQLQUERY_TEMPLATE = "GraphQLQueryResolver.ftl";
    public static final String GRAPHQLMUTATION_TEMPLATE = "GraphQLMutationResolver.ftl";
    public static final String GRAPHQL_TEMPLATE = "graphql.ftl";
    /**
     * mapper xml接口模板
     */
    public static final String MAPPERXML_TEMPLATE = "mapperXml.ftl";
}
