package com.rains.graphql.system.query;


import com.rains.graphql.system.domain.Menu;
import com.rains.graphql.system.manager.UserManager;
import com.rains.graphql.system.service.MenuService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MenuQuery implements GraphQLQueryResolver {


    @Autowired
    private UserManager userManager;
    @Autowired
    private MenuService menuService;



    @RequiresPermissions("menu:view")
    public List<Menu> list(Menu menu){
       return menuService.findMenuList(menu);
    }




}
