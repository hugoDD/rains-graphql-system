package com.rains.graphql.system.service;


import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.domain.Tree;
import com.rains.graphql.system.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DeptService extends IService<Dept> {

    Tree<Dept> findRoot(QueryRequest request, Dept dept);

    Map<String, Object> findDepts(QueryRequest request, Dept dept);

    List<Dept> findDepts(Dept dept, QueryRequest request);

    void createDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDepts(String[] deptIds);
}
