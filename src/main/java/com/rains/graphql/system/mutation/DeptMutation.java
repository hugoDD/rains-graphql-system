package com.rains.graphql.system.mutation;

import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.Dept;
import com.rains.graphql.system.service.DeptService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Component
public class DeptMutation implements GraphQLMutationResolver {



    @Autowired
    private DeptService deptService;


    @Log("新增部门")
    @PostMapping
    @RequiresPermissions("dept:add")
    public Dept addDept(@Valid Dept dept) throws SysException {
        try {
            this.deptService.createDept(dept);
        } catch (Exception e) {
            String  message = "新增部门失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return dept;
    }

    @Log("删除部门")
  //  @DeleteMapping("/{deptIds}")
    @RequiresPermissions("dept:delete")
    public boolean deleteDepts( String[] deptIds) throws SysException {
        try {
          //  String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(deptIds);
        } catch (Exception e) {
            String  message = "删除部门失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    @Log("修改部门")
    @PutMapping
    @RequiresPermissions("dept:update")
    public Dept updateDept(@Valid Dept dept) throws SysException {
        try {
            this.deptService.updateDept(dept);
        } catch (Exception e) {
            String  message = "修改部门失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return dept;
    }

    @RequiresPermissions("dept:export")
    public void deptExport(Dept dept, QueryRequest request, DataFetchingEnvironment env) throws SysException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, GraphQLHttpUtil.getResponse(env)).downXlsx(depts, false);
        } catch (Exception e) {
            log.error("导出Excel失败", e);
            throw new SysException("导出Excel失败");
        }
    }
}
