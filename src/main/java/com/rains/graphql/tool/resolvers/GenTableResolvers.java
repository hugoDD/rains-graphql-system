package com.rains.graphql.tool.resolvers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rains.graphql.common.utils.JsonMapper;
import com.rains.graphql.tool.entity.GenTable;
import com.rains.graphql.tool.entity.GenTableColumn;
import com.rains.graphql.tool.service.IGenTableColumnService;
import com.rains.graphql.tool.service.IGenTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GenTableResolvers implements GraphQLResolver<GenTable> {

    private static Map<String, Map<String, Object>> uiMap;

    static {
        try {
            uiMap = parseUI();
        } catch (IOException e) {
            log.error("init ui error", uiMap);
        }
    }

    @Autowired
    private IGenTableService genTableService;
    @Autowired
    private IGenTableColumnService genTableColumnService;

    private static Map<String, Map<String, Object>> parseUI() throws IOException {

        //List<Map<String, Object>> list = new ArrayList<>();

//        ClassPathResource input = new ClassPathResource("uiconfig/inputComponents.json");
//        ClassPathResource select = new ClassPathResource("uiconfig/selectComponents.json");
//        ClassPathResource layout = new ClassPathResource("uiconfig/layoutComponents.json");

        ClassPathResource drawingItems = new ClassPathResource("uiconfig/drawingItems.json");
        ClassPathResource formConf = new ClassPathResource("uiconfig/formConf.json");

        JsonMapper jsonMapper = new JsonMapper();
        Map<String, Map<String, Object>> uiMap = new HashMap<>();


//        List<Map<String, Object>> inputlist = jsonMapper.getJacksonMapper().readValue(input.getInputStream(), List.class);
//        List<Map<String, Object>> selectlist = jsonMapper.getJacksonMapper().readValue(select.getInputStream(), List.class);
//        List<Map<String, Object>> layoutlist = jsonMapper.getJacksonMapper().readValue(layout.getInputStream(), List.class);
//
//
//        list.addAll(inputlist);
//        list.addAll(selectlist);
//        list.addAll(layoutlist);

        List<Map<String, Object>> list = jsonMapper.getJacksonMapper().readValue(drawingItems.getInputStream(), List.class);
        Map<String, Object> formConfMap = jsonMapper.getJacksonMapper().readValue(formConf.getInputStream(), Map.class);

        list.forEach(s -> {
            if ("date".equals(s.get("tagIcon"))) {
                uiMap.put("datetime", s);
            } else {
                uiMap.put((String) s.get("tagIcon"), s);
            }
        });
        uiMap.put("formConf", formConfMap);
        return uiMap;
    }

    public Map<String, String> preview(GenTable genTable) {

        return genTableService.previewCode(genTable.getTableId());
    }

    public Map<String, Object> formConf(GenTable genTable) {
        return uiMap.get("formConf");
    }

    public List<Map<String, Object>> preGenViewUI(GenTable genTable) {

        List<Map<String, Object>> drawinglist = genTableService.preViewUI(genTable.getTableId(), uiMap);
        return drawinglist;
    }

    public List<GenTableColumn> getColumns(GenTable genTable) {
        LambdaQueryWrapper<GenTableColumn> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GenTableColumn::getTableId, genTable.getTableId());
        return genTableColumnService.list(queryWrapper);
    }
}
