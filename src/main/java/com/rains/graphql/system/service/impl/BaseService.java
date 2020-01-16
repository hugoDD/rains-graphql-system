package com.rains.graphql.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.domain.RainsConstant;
import com.rains.graphql.common.graphql.GraphQLHttpUtil;
import com.rains.graphql.common.rsql.RsqlToMybatisPlusWrapper;
import com.rains.graphql.common.utils.SortUtil;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.IBaseService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T> implements IBaseService<T> {

    public PageData<T> query(QueryRequest request) {
        Wrapper<T> queryWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass());
        Page<T> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", RainsConstant.ORDER_DESC, true);
        IPage<T> pageData = this.page(page, queryWrapper);
        return new PageData<>(pageData.getTotal(), pageData.getRecords());
    }

    public boolean delete(QueryRequest<T> request) {
        if (StringUtils.isNotEmpty(request.getFilter())) {
            Wrapper<T> updateWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass(), true);
            return this.remove(updateWrapper);
        } else if (request.getIds() != null) {
            return request.getIds().length == 1 ? this.removeById(request.getIds()[0]) : this.removeByIds(Arrays.asList(request.getIds()));
        } else {
            throw new RuntimeException("del filter or ids must not null !!");
        }


    }

    public boolean saveOrUpdate(QueryRequest<T> request) {
        if (StringUtils.isEmpty(request.getFilter())) {
            return this.saveOrUpdate(request.getData());
        } else {
            Wrapper<T> updateWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass(), true);
            return this.saveOrUpdate(request.getData(), updateWrapper);
        }

    }

    public void export(QueryRequest<T> request, DataFetchingEnvironment env) {
        try {
            Wrapper<T> queryWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass());
            List<T> list = this.list(queryWrapper);
            ExcelKit.$Export(currentModelClass(), GraphQLHttpUtil.getResponse(env)).downXlsx(list, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);

        }
    }


}
