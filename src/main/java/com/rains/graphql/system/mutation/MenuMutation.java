package com.rains.graphql.system.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.SysException;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.system.domain.Menu;
import com.rains.graphql.system.service.MenuService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class MenuMutation implements GraphQLMutationResolver {
    @Autowired
    private MenuService menuService;

    @Log("菜单[#request.opt]操作系统日志")
    @RequiresPermissions("system:menu:[#request.opt]")
    public boolean menuBaseMutation(QueryRequest request, DataFetchingEnvironment env) {
        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> menuService.export(q, env);
            request.opt("export", exportOpt);
        }

        return menuService.baseOpt(request);

    }

    @Log("新增菜单/按钮")
    //@PostMapping
    @RequiresPermissions("menu:add")
    public Menu addMenu(@Valid Menu menu) throws SysException {
        try {
            this.menuService.createMenu(menu);
        } catch (Exception e) {
            String message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new SysException(message);
        }

        return menu;
    }

    @Log("删除菜单/按钮")
    //@DeleteMapping("/{menuIds}")
    @RequiresPermissions("menu:delete")
    public void deleteMenus(@NotBlank(message = "{required}") String[] menuIds) throws SysException {
        try {
            //String[] ids = menuIds.split(StringPool.COMMA);
            this.menuService.deleteMeuns(menuIds);
        } catch (Exception e) {
            String message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new SysException(message);
        }
    }

    @Log("修改菜单/按钮")
    //@PutMapping
    @RequiresPermissions("menu:update")
    public Menu updateMenu(@Valid Menu menu) throws SysException {
        try {
            this.menuService.updateMenu(menu);
        } catch (Exception e) {
            String message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new SysException(message);
        }
        return menu;
    }

    public void menuExport(Menu menu, DataFetchingEnvironment env) throws SysException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
            ExcelKit.$Export(Menu.class, GraphQLHttpUtil.getResponse(env)).downXlsx(menus, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new SysException(message);
        }
    }
}
