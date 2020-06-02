package com.rains.graphql.common.graphql.configuration;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NettySocketProperties.class)
public class NettySocketConfig {



    @Bean
    public SocketIOServer socketIOServer(NettySocketProperties nettySocketProperties) throws Exception {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(nettySocketProperties.getHost());
        config.setPort(nettySocketProperties.getPort());
        config.setMaxFramePayloadLength(nettySocketProperties.getMaxFramePayloadLength());
        config.setMaxHttpContentLength(nettySocketProperties.getMaxHttpContentLength());
        config.setBossThreads(nettySocketProperties.getBossCount());
        config.setWorkerThreads(nettySocketProperties.getWorkCount());
        config.setAllowCustomRequests(nettySocketProperties.isAllowCustomRequests());
        config.setUpgradeTimeout(nettySocketProperties.getUpgradeTimeout());
        config.setPingTimeout(nettySocketProperties.getPingTimeout());
        config.setPingInterval(nettySocketProperties.getPingInterval());

        //该处进行身份验证
        config.setAuthorizationListener(handshakeData -> {
            return true;
        });
        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    //开启注解
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }


}


