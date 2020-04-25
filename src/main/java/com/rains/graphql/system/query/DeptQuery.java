package com.rains.graphql.system.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Dept;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Validated
@Component
public class DeptQuery implements GraphQLQueryResolver {
    @Autowired
    private DeptService deptService;

    public PageData<Dept> deptPage(QueryRequest request, Dept dept) {
        List<Dept> depts = deptService.findDepts(dept, request);
        return new PageData<>(depts.size(), depts);
    }

    public PageData<Dept> deptPageQuery(QueryRequest request) {
        PageData<Dept> depts = deptService.query(request);
        return depts;
    }

    public Dept deptDetail(Dept dept) {
        return deptService.getOne(new QueryWrapper<>(dept));
    }

}
