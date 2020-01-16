package com.rains.graphql.system.service.impl;


import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.generator.GeneratorConstant;
import com.rains.graphql.common.utils.AddressUtil;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.system.domain.Column;
import com.rains.graphql.system.domain.GeneratorConfig;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 代码生成器工具类
 *
 * @author MrBird
 */
@Slf4j
@Service
public class GeneratorHelper {

    private static String getFilePath(GeneratorConfig configure, String packagePath, String suffix, boolean serviceInterface) {
        String filePath = GeneratorConstant.TEMP_PATH + configure.getJavaPath() +
                packageConvertPath(configure.getBasePackage() + "." + packagePath);
        if (serviceInterface) {
            filePath += "I";
        }
        filePath += configure.getClassName() + suffix;
        return filePath;
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    public void generateEntityFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.JAVA_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getEntityPackage(), suffix, false);
        String templateName = GeneratorConstant.ENTITY_TEMPLATE;
        File entityFile = new File(path);
        JSONObject data = toJSONObject(configure);
        data.put("hasDate", false);
        data.put("hasBigDecimal", false);
        columns.forEach(c -> {
            c.setField(SysUtil.camelToUnderscore(StringUtils.lowerCase(c.getName())));
            try {
            if (StringUtils.containsAny(c.getType(), "date", "datetime", "timestamp")) {
                    data.put("hasDate", true);
            }
            if (StringUtils.containsAny(c.getType(), "decimal", "numeric")) {
                data.put("hasBigDecimal", true);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        data.put("columns", columns);
        this.generateFileByTemplate(templateName, entityFile, data);
    }

    public void generateMapperFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.MAPPER_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getMapperPackage(), suffix, false);
        String templateName = GeneratorConstant.MAPPER_TEMPLATE;
        File mapperFile = new File(path);
        generateFileByTemplate(templateName, mapperFile, toJSONObject(configure));
    }

    public void generateServiceFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.SERVICE_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getServicePackage(), suffix, true);
        String templateName = GeneratorConstant.SERVICE_TEMPLATE;
        File serviceFile = new File(path);
        generateFileByTemplate(templateName, serviceFile, toJSONObject(configure));
    }

    public void generateServiceImplFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.SERVICEIMPL_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getServiceImplPackage(), suffix, false);
        String templateName = GeneratorConstant.SERVICEIMPL_TEMPLATE;
        File serviceImplFile = new File(path);
        generateFileByTemplate(templateName, serviceImplFile, toJSONObject(configure));
    }

    public void generateControllerFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.CONTROLLER_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getControllerPackage(), suffix, false);
        String templateName = GeneratorConstant.CONTROLLER_TEMPLATE;
        File controllerFile = new File(path);
        generateFileByTemplate(templateName, controllerFile, toJSONObject(configure));
    }

    public void generateMapperXmlFile(List<Column> columns, GeneratorConfig configure) throws Exception {
        String suffix = GeneratorConstant.MAPPERXML_FILE_SUFFIX;
        String path = getFilePath(configure, configure.getMapperXmlPackage(), suffix, false);
        String templateName = GeneratorConstant.MAPPERXML_TEMPLATE;
        File mapperXmlFile = new File(path);
        JSONObject data = toJSONObject(configure);
        columns.forEach(c -> c.setField(SysUtil.camelToUnderscore(StringUtils.lowerCase(c.getName()))));
        data.put("columns", columns);
        generateFileByTemplate(templateName, mapperXmlFile, data);
    }

    @SuppressWarnings("all")
    private void generateFileByTemplate(String templateName, File file, Object data) throws Exception {
        Template template = getTemplate(templateName);
        Files.createParentDirs(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8), 10240)) {
            template.process(data, out);
        } catch (Exception e) {
            String message = "代码生成异常";
            log.error(message, e);
            throw new Exception(message);
        }
    }

    private JSONObject toJSONObject(Object o) {
        return JSONObject.parseObject(JSONObject.toJSON(o).toString());
    }

    private Template getTemplate(String templateName) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        String templatePath = GeneratorHelper.class.getResource("/generator/templates/").getPath();
        File file = new File(templatePath);
        if (!file.exists()) {
            templatePath = System.getProperties().getProperty(RainsConstant.JAVA_TEMP_DIR);
            file = new File(templatePath + File.separator + templateName);
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(AddressUtil.class.getClassLoader().getResourceAsStream("classpath:generator/templates/" + templateName)), file);
        }
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        configuration.setDefaultEncoding(RainsConstant.UTF8);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return configuration.getTemplate(templateName);

    }
}
