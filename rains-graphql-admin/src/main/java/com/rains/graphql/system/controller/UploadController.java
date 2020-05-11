package com.rains.graphql.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.ResultResponse;
import com.rains.graphql.common.file.FileUploadUtils;
import com.rains.graphql.common.properties.RainsGraphqlProperties;
import com.rains.graphql.common.utils.SysUtil;
import com.rains.graphql.system.domain.Dept;
import com.rains.graphql.system.domain.Role;
import com.rains.graphql.system.domain.User;
import com.rains.graphql.system.domain.UserRole;
import com.rains.graphql.system.service.*;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@Slf4j
public class UploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private IPostService postService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/system/user/importData")
    public ResultResponse importData(MultipartFile file, boolean updateSupport) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        List<User> successList = new ArrayList<>();
        ExcelKit excelKit = ExcelKit.$Import(User.class);
        excelKit.readXlsx(file.getInputStream(), new ExcelReadHandler<User>() {
            @Override
            public void onSuccess(int sheetIndex, int rowIndex, User entity) {
                entity.setPassword("123456");
                userService.save(entity);
                Role role = roleService.findByName(entity.getRoleName());
                UserRole userRole = new UserRole();
                userRole.setRoleId(role.getRoleId());
                userRole.setUserId(entity.getUserId());
                userRoleService.save(userRole);
                LambdaQueryWrapper<Dept> deptQuery = Wrappers.lambdaQuery();
                deptQuery.eq(Dept::getDeptName, entity.getDeptName());
                Dept dept = deptService.getOne(deptQuery);
                if (Objects.nonNull(role)) {
                    entity.setRoleId(role.getRoleId() + "");
                }
                if (Objects.nonNull(dept)) {
                    entity.setDeptId(dept.getDeptId());
                }
                successList.add(entity);
            }

            @Override
            public void onError(int sheetIndex, int rowIndex, List<ExcelErrorField> errorFields) {
                log.error("sheetIndex:{}, rowIndex:{} , eerorFilelds: {}", sheetIndex, rowIndex, errorFields);
            }
        });


        return resultResponse.message("导入成功");
    }

    /**
     * 头像上传
     */
    @Log("用户头像上传")
    @PostMapping("/system/user/profile/avatar")
    public ResultResponse avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        ResultResponse res = new ResultResponse();
        if (!file.isEmpty()) {
            String userName = SysUtil.getCurrentUserName();
            String avatar = FileUploadUtils.upload(RainsGraphqlProperties.getAvatarPath(), file);
            userService.updateAvatar(userName, avatar);
            Map<String, String> map = new HashMap<>();
            map.put("imgUrl", avatar);
            res.data(map);

        }
        res.setCode(500);
        return res.message("上传图片异常，请联系管理员");
    }
}
