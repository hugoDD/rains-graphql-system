package com.rains.graphql.system.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@Component
public class RoleQuery implements GraphQLQueryResolver {

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("role:view")
    public PageData<Role> rolePageQuery(QueryRequest queryRequest) {
        PageData<Role> page = roleService.query(queryRequest);
        return page;
    }

    //@GetMapping
    @RequiresPermissions("role:view")
    public PageData<Role> rolePage(QueryRequest queryRequest, Role role) {
        if (queryRequest == null) {
            queryRequest = new QueryRequest();
        }
        if (role == null) {
            role = new Role();
        }
        IPage<Role> page = roleService.findRoles(role, queryRequest);
        return new PageData<Role>(page.getTotal(), page.getRecords());
    }

    // @GetMapping("check/{roleName}")
    public Role roleDetail(@NotBlank(message = "{required}") String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result;
    }

//    @GetMapping("menu/{roleId}")
//    public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
//        List<RoleMenu> list = this.roleMenuServie.getRoleMenusByRoleId(roleId);
//        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
//    }


//    @PostMapping("excel")
//    @RequiresPermissions("role:export")
//    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws FebsException {
//        try {
//            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
//            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
//        } catch (Exception e) {
//            message = "导出Excel失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
}
