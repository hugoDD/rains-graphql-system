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
import com.rains.graphql.common.utils.BeanMapUtils;
import com.rains.graphql.common.utils.SortUtil;
import com.rains.graphql.common.utils.SpringContextUtil;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.IBaseService;
import com.wuwenze.poi.ExcelKit;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class BaseService<S extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T> implements IBaseService<T> {

    public Function<QueryRequest, List<T>> optReq = (QueryRequest request) -> BeanMapUtils.objToBeans(request.getDatas(), currentModelClass());
    @Autowired
    protected S baseMapper;
    Consumer<QueryRequest<T>> inser = q -> {
    };
    ExpressionParser parser = new SpelExpressionParser();
    //  public Function<List<T>,Boolean> optSvc=( datas) ->BeanMapUtils.objToBeans(request.getDatas(), currentModelClass());
    EvaluationContext context2 = new StandardEvaluationContext();

    @Override
    public S getBaseMapper() {
        return baseMapper;
    }

    public boolean baseOpt(QueryRequest request) {


        List<T> listData = BeanMapUtils.objToBeans(request.getDatas(), currentModelClass());
        if (Objects.isNull(listData)) {
            return false;
        }
        if (request.getConsumer() != null) {
            request.getConsumer().accept(request);
        }
        switch (request.getOpt()) {
            case "insert": {
                if (listData != null && listData.size() > 0) {
                    super.saveBatch(listData);
                } else {
                    super.save(listData.get(0));
                }
                break;
            }
            case "update": {
                if (listData != null && listData.size() > 1) {
                    super.saveOrUpdateBatch(listData);
                } else {
                    super.saveOrUpdate(listData.get(0));
                }
                break;
            }
            case "delete": {
                delete(request);
                break;
            }
            default: {
                request.execute();
            }
        }

        if (request.getChild() != null) {
            for (int i = 0; i < listData.size(); i++) {
                if (i == 0) {
                    context2.setVariable("data", listData.get(0));
                }
                context2.setVariable("data" + i, listData.get(i));
            }
            context2.setVariable("listdata", listData);
            List<QueryRequest> childes = request.getChild();
            childes.stream().sorted(Comparator.comparing(QueryRequest::getRunOrder));
            childes.stream().sorted(Comparator.comparing(QueryRequest::getRunOrder)).forEach((v) -> {
                String svc = v.getSvc();
                System.out.println("当前的服务:" + svc + " opt=" + v.getOpt());
                if (com.rains.graphql.common.utils.StringUtils.isEmpty(svc)) {
                    return;
                }
                svc = svc.contains("Service") ? svc : svc + "ServiceImpl";
                Object[] objs = v.getDatas();
                if (objs != null && objs.length > 0) {
                    objs = Stream.of(objs).filter(e -> e instanceof Map).map(e -> transform((Map<String, Object>) e, context2)).toArray();
                }
                IBaseService baseService = SpringContextUtil.getBean(svc, IBaseService.class);
                baseService.baseOpt(v);
            });
        }

        return true;
    }

    public Map<String, Object> transform(Map<String, Object> e, EvaluationContext context2) {
        e.forEach((k, v) -> {
            if (v instanceof String && ((String) v).contains("#")) {
                v = parser.parseExpression((String) v).getValue(context2, String.class);
                e.put(k, v);
            }
        });
        return e;
    }

    public PageData<T> query(QueryRequest request) {
        Wrapper<T> queryWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass());
        Page<T> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", RainsConstant.ORDER_DESC, true);
        IPage<T> pageData = this.page(page, queryWrapper);
        return new PageData<>(pageData.getTotal(), pageData.getRecords());
    }

    @Transactional
    public boolean delete(QueryRequest<T> request) {
        if (request.getIds() != null && request.getIds().length > 0) {
            return request.getIds().length == 1 ? this.removeById(request.getIds()[0]) : this.removeByIds(Arrays.asList(request.getIds()));
        } else if (StringUtils.isNotEmpty(request.getFilter())) {
            Wrapper<T> updateWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass(), true);
            return this.remove(updateWrapper);
        } else {
            throw new RuntimeException("del filter or ids must not null !!");
        }


    }

    @Transactional
    public boolean saveOrUpdate(QueryRequest<T> request) {
        if (StringUtils.isEmpty(request.getFilter())) {
            return this.saveOrUpdate(request.getData());
        } else {
            Wrapper<T> updateWrapper = RsqlToMybatisPlusWrapper.getInstance().rsqlToWrapper(request.getFilter(), currentModelClass(), true);
            return this.saveOrUpdate(request.getData(), updateWrapper);
        }

    }

    @Transactional
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
