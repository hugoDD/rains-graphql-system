package com.rains.graphql.tool.resolvers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rains.graphql.tool.entity.GenTable;
import com.rains.graphql.tool.entity.GenTableColumn;
import com.rains.graphql.tool.service.IGenTableColumnService;
import com.rains.graphql.tool.service.IGenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GenTableResolvers  implements GraphQLResolver<GenTable> {

    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    public Map<String, String> preview(GenTable genTable) {

        return genTableService.previewCode(genTable.getTableId());
    }

    public List<GenTableColumn> getColumns(GenTable genTable){
        LambdaQueryWrapper<GenTableColumn> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GenTableColumn::getTableId,genTable.getTableId());
       return genTableColumnService.list(queryWrapper);
    }
}
