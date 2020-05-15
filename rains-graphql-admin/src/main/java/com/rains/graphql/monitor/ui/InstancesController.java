package com.rains.graphql.monitor.ui;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@RestController
@RequestMapping("/instances")
public class InstancesController {
    RestTemplate restTemplate = new RestTemplate();
    String[] actuatorMimeTypes = {
            "application/vnd.spring-boot.actuator.v2+json",
            "application/vnd.spring-boot.actuator.v1+json",
            "application/json"
    };

    @GetMapping(path = "/{hostname}/actuator/{type}/**")
    public ResponseEntity<String> actuator(HttpServletRequest request, @PathVariable("hostname") String hostname, @PathVariable("type") String type) {
       String tag =request.getParameter("tag");
       String uri = request.getRequestURI().replace("/instances/"+hostname,"");
       if(StringUtils.isEmpty(tag)){
           uri = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+uri;
       }else {
           uri = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+uri+"?tag="+tag;
       }


         System.out.println(uri);

//        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory ) restTemplate.getRequestFactory().;
//
//        factory.setConnectTimeout(3000);//连接超时时间
//
//        factory.setReadTimeout(10000);//响应超时时间

        ClientHttpRequestInterceptor acceptHeader = new AcceptHeaderHttpRequestInterceptor(
                Arrays.stream(actuatorMimeTypes).collect(Collectors.joining(",")));


        restTemplate.setInterceptors(singletonList(acceptHeader));

        return  restTemplate.getForEntity(uri,String.class);

//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", Arrays.stream(actuatorMimeTypes).collect(Collectors.joining(",")));
//
//        HttpEntity<String> entity = new HttpEntity<>("body", headers);
//
//        return restTemplate.getForObject().exchange(uri, HttpMethod.resolve(request.getMethod()),entity,String.class);
//
//        restTemplate.getForEntity(uri,String.class);
        //return  restTemplate.getForEntity(uri,String.class);

    }

    class AcceptHeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {
        private final String headerValue;

        public AcceptHeaderHttpRequestInterceptor(String headerValue) {
            this.headerValue = headerValue;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {

            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            requestWrapper.getHeaders().set("Accept",this.headerValue);


            return execution.execute(requestWrapper, body);
        }


    }
}
