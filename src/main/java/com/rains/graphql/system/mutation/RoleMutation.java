/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.rains.graphql.system.mutation;

import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.service.RoleService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Component
public class RoleMutation implements GraphQLMutationResolver {
    @Autowired
    private RoleService roleService;


    @Log("新增角色")
    @RequiresPermissions("role:add")
    public Role addRole(@Valid Role role) throws SysException {
        try {
            this.roleService.createRole(role);
        } catch (Exception e) {
            String message = "新增角色失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return role;
    }

    @Log("删除角色")
    @RequiresPermissions("role:delete")
    public boolean deleteRoles(@NotBlank(message = "{required}")  String[] roleIds) throws SysException {
        try {
            //String[] ids = roleIds.split(StringPool.COMMA);
            this.roleService.deleteRoles(roleIds);
        } catch (Exception e) {
            String message = "删除角色失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return true;
    }

    @Log("修改角色")
    @RequiresPermissions("role:update")
    public Role updateRole(Role role) throws SysException {
        try {
            this.roleService.updateRole(role);
        } catch (Exception e) {
            String message = "修改角色失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return role;
    }

    @RequiresPermissions("role:export")
    public void roleExport(QueryRequest queryRequest, Role role, DataFetchingEnvironment env) throws SysException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(Role.class, GraphQLHttpUtil.getResponse(env)).downXlsx(roles, false);
        } catch (Exception e) {
           String message = "导出Excel失败";
            log.error(message, e);
            throw new SysException(message);
        }
    }
}
