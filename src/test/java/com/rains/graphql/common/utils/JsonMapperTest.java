package com.rains.graphql.common.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperTest {

    @Test
    public void parseJson() throws IOException {
        ClassPathResource input = new ClassPathResource("uiconfig/inputComponents.json");
        ClassPathResource select = new ClassPathResource("uiconfig/selectComponents.json");
        InputStream inputStream =input.getInputStream();

        JsonMapper jsonMapper = new JsonMapper();
        List<Map<String,Object>> inputlist = jsonMapper.getJacksonMapper().readValue(inputStream, List.class);
        System.out.println(inputlist);

        List<Map<String,Object>> selectlist = jsonMapper.getJacksonMapper().readValue(select.getInputStream(), List.class);
        System.out.println(selectlist);
    }

}