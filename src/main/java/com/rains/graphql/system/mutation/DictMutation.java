package com.rains.graphql.system.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.Dict;
import com.rains.graphql.system.service.DictService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@Component
public class DictMutation implements GraphQLMutationResolver {


    @Autowired
    private DictService dictService;


    @Log("新增字典")
    @PostMapping
    @RequiresPermissions("dict:add")
    public Dict addDict(@Valid Dict dict) throws SysException {
        try {
            this.dictService.createDict(dict);
        } catch (Exception e) {
            String message = "新增字典成功";
            log.error(message, e);
            throw new SysException(message);
        }

        return dict;
    }

    @Log("删除字典")
    @DeleteMapping("/{dictIds}")
    @RequiresPermissions("dict:delete")
    public boolean deleteDicts(@NotBlank(message = "{required}") String[] dictIds) throws SysException {
        try {
            //String[] ids = dictIds.split(StringPool.COMMA);
            this.dictService.deleteDicts(dictIds);
        } catch (Exception e) {
            String message = "删除字典成功";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    @Log("修改字典")
    @RequiresPermissions("dict:update")
    public Dict updateDict(@Valid Dict dict) throws SysException {
        try {
            this.dictService.updateDict(dict);
        } catch (Exception e) {
            String message = "修改字典成功";
            log.error(message, e);
            throw new SysException(message);
        }
        return dict;
    }

    @RequiresPermissions("dict:export")
    public void dictExport(QueryRequest request, Dict dict, DataFetchingEnvironment env) throws SysException {
        try {
            List<Dict> dicts = this.dictService.findDicts(request, dict).getRecords();
            ExcelKit.$Export(Dict.class, GraphQLHttpUtil.getResponse(env)).downXlsx(dicts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new SysException(message);
        }
    }


}
