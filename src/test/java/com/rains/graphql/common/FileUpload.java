package com.rains.graphql.common;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class FileUpload {

    @Test
    public void testUpload() throws Exception {
        FileUpload.class.getClassLoader().getResource("test.txt").getPath();

        String url = "http://172.25.191.228/mp/file/notice/upload.do?token=d41e41ce9eed43098730c2a4a65972f8&r=1584411009486";
        String filePath = "D:\\分析日志.xls";

        byte[] bytes = FileUtils.readFileToByteArray(new File(filePath));

        FileUtils.writeByteArrayToFile(new File(""), bytes);


        RestTemplate restTemplate = new RestTemplate();
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "D:\\分析日志.xls";
            }
        };
        // FileSystemResource resource = new FileSystemResource(new File(filePath));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("uploadfile", resource);
        param.add("filename", "分析日志.xls");


        String string = restTemplate.postForObject(url, param, String.class);
        System.out.println(string);
    }

}
