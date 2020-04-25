package com.rains.graphql.common.utils;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMapperTest {

    @Test
    public void testZone() {
        System.out.println(LocalDateTime.now());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime time = LocalDateTime.now();
//        String localTime = df.format(time);
        LocalDateTime ldt = LocalDateTime.parse("2017-09-28 17:07:05", df);
        // System.out.println("LocalDateTime转成String类型的时间："+localTime);
        System.out.println("String类型的时间转成LocalDateTime：" + ldt);

    }

    @Test
    public void parseJson() throws IOException {
        ClassPathResource input = new ClassPathResource("uiconfig/inputComponents.json");
        ClassPathResource select = new ClassPathResource("uiconfig/selectComponents.json");
        InputStream inputStream = input.getInputStream();

        JsonMapper jsonMapper = new JsonMapper();
        List<Map<String, Object>> inputlist = jsonMapper.getJacksonMapper().readValue(inputStream, List.class);

        Map<String, Map<String, Object>> inputMap = new HashMap<>();
        inputlist.forEach(i -> {
            inputMap.put((String) i.get("tagIcon"), i);
        });
        System.out.println(inputMap);

        List<Map<String, Object>> selectlist = jsonMapper.getJacksonMapper().readValue(select.getInputStream(), List.class);
        System.out.println(selectlist);


        selectlist.forEach(s -> {
            inputMap.put((String) s.get("tagIcon"), s);
        });
        System.out.println(inputMap);
    }


}
