package com.rains.graphql.tool.mutation;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.generator.GenConstants;
import com.rains.graphql.common.generator.VelocityUtils;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.common.utils.BeanMapUtils;
import com.rains.graphql.common.utils.FileUtil;
import com.rains.graphql.common.utils.StringUtils;
import com.rains.graphql.tool.entity.GenTable;
import com.rains.graphql.tool.entity.GenTableColumn;
import com.rains.graphql.tool.service.IGenTableService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.mutation.BaseMutation;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成业务表 Mutation
 *
 * @author hugoDD
 * @date 2020-01-19 14:14:07
 */
@Slf4j
@Component
public class GenTableMutation implements GraphQLMutationResolver {


    @Autowired
    private IGenTableService genTableService;
    private Consumer<QueryRequest> exportOpt;

    Consumer<QueryRequest> importTable = q -> {
        List<GenTable> listData = BeanMapUtils.objToBeans(q.getDatas(), GenTable.class);
        genTableService.importGenTable(listData);
    };

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("tool:gen:[#request.opt]")
    public boolean genTableBaseMutation(QueryRequest request, DataFetchingEnvironment env) throws Exception {
        if (exportOpt == null) {
            exportOpt = q -> genTableService.export(q, env);
        }
        Consumer<QueryRequest> genCode = q -> {
            HttpServletResponse response = GraphQLHttpUtil.getResponse(env);
            byte[] data = generatorCode(request.getIds());
            try {
                FileUtil.download(data, "rains_code.zip", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
//        if ("genCode".equals(request.getOpt())) {
//            GraphQLServletContext context = env.getContext();
//            HttpServletResponse response = context.getHttpServletResponse();
//            byte[] data = generatorCode(request.getIds());
//            FileUtil.download(data, "rains_code.zip", response);
//            return true;
//        }

        request.opt("export", exportOpt).opt("importTable", importTable).opt("genCode", genCode);

        return genTableService.baseOpt(request);

//        switch (request.getOpt()) {
//            case "export":
//                genTableService.export(request, env);
//                return true;
//            case "importTable":
//                List<GenTable> listData = BeanMapUtils.objToBeans(request.getDatas(), GenTable.class);
//                return genTableService.importGenTable(listData);
//
//            default:
//                return genTableService.baseOpt(request);
//        }
    }

    /**
     * 生成zip文件
     */
    private void genCode(QueryRequest request, HttpServletResponse response) {


        try {
            byte[] data = generatorCode(request.getIds());
            FileUtil.download(data,"rains_code.zip",response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 批量生成代码
     *
     * @param tableIds 表数组
     * @return 数据
     */

    public byte[] generatorCode(Long[] tableIds) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Long tableId : tableIds) {
            generatorCode(tableId, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(Long tableId, ZipOutputStream zip) {
        GenTable genTable = genTableService.getById(tableId);
        Map<String, String> codeMap = genTableService.previewCode(tableId);
        for (Map.Entry<String, String> code : codeMap.entrySet()) {
            try {
                // 添加到zip
                String fileName = VelocityUtils.getFileName(code.getKey(), genTable);
                System.out.println("filename:" + fileName);
                if (StringUtils.isEmpty(fileName)) {
                    continue;
                }
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(code.getValue(), zip, GenConstants.UTF8);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + genTable.getTableName(), e);
            }
        }


    }
}
