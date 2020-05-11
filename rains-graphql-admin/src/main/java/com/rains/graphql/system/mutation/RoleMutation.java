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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.domain.RoleDept;
import com.rains.graphql.system.service.IRoleDeptService;
import com.rains.graphql.system.service.RoleMenuServie;
import com.rains.graphql.system.service.RoleService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Component
public class RoleMutation implements GraphQLMutationResolver {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuServie roleMenuServie;

    @Autowired
    private IRoleDeptService roleDeptService;


    @Log("角色[#request.opt]操作系统日志")
    @RequiresPermissions("system:role:[#request.opt]")
    public boolean roleBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> roleService.export(q, env);
            request.opt("export", exportOpt);
        } else if ("dataScope".equals(request.getOpt())) {

            Consumer<QueryRequest> dataScopeOpt = q -> {
                if (Objects.isNull(request.getDatas()) || request.getDatas().length == 0) {
                    return;
                }
                Map<String, Object> role = (Map<String, Object>) request.getDatas()[0];
                Long roleId = Long.parseLong(role.get("roleId").toString());
                String dataScope = role.get("dataScope").toString();
                Role entity = new Role();
                entity.setRoleId(roleId);
                entity.setDataScope(dataScope);

                roleService.updateById(entity);

                QueryWrapper<RoleDept> wrapper = Wrappers.query();

                wrapper.lambda().eq(RoleDept::getRoleId, roleId);
                roleDeptService.remove(wrapper);

                if (Objects.nonNull(role.get("deptIds"))) {
                    List<RoleDept> list = new ArrayList<>();
                    List<Integer> deptIds = (List<Integer>) role.get("deptIds");
                    deptIds.forEach(deptId -> {
                        RoleDept roleDept = new RoleDept();
                        roleDept.setRoleId(roleId);
                        roleDept.setDeptId(Integer.toUnsignedLong(deptId));
                        list.add(roleDept);
                    });
                    if (!list.isEmpty()) {
                        roleDeptService.saveBatch(list);
                    }

                }

            };

            request.opt("dataScope", dataScopeOpt);
        }

        return roleService.baseOpt(request);

    }


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
    public boolean deleteRoles(@NotBlank(message = "{required}") String[] roleIds) throws SysException {
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
