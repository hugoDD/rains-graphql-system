package com.rains.graphql.common.config;

import com.rains.graphql.common.handler.MyHandler;
import com.rains.graphql.common.handler.MyHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration // 配置类
@EnableWebSocket // 声明支持websocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册websocket实现类，指定参数访问地址;allowed-origins="*" 允许跨域
        // addHandler是增加处理接口和设定URL
        // addInterceptors是增加拦截器处理（可以不用）
        registry.addHandler(myHandler(), "/ws").addInterceptors(myHandshake()).setAllowedOrigins("*");
        registry.addHandler(myHandler(), "/sockjs-node/info").addInterceptors(myHandshake()).withSockJS();

//        registry.addHandler(myHandler(), "/ws2").setAllowedOrigins("*");
//        registry.addHandler(myHandler(), "/sockjs/ws2").setAllowedOrigins("*").withSockJS();
    }

    @Bean
    public MyHandler myHandler() {
        return new MyHandler();
    }

    @Bean
    public MyHandshakeInterceptor myHandshake() {
        return new MyHandshakeInterceptor();
    }
}