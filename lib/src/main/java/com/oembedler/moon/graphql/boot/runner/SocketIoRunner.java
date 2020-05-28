package com.oembedler.moon.graphql.boot.runner;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;


/**
 * SocketIo启动类
 */
@Slf4j
@Component
@Order(value = 1)
public class SocketIoRunner implements CommandLineRunner {

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) {
        socketIOServer.start();
        log.info(" __    ___   _      ___   _     ____ _____  ____ ");
        log.info("                                                      ");
        log.info("socket.io启动成功！，时间：" + LocalDateTime.now());
        log.info("                                                      ");
        log.info(" __    ___   _      ___   _     ____ _____  ____ ");
    }

    @PreDestroy
    public void destroy(){
        log.info(" __    ___   _      ___   _     ____ _____  ____ ");
        log.info("                                                      ");
        log.info("socket.io释放！，时间：" + LocalDateTime.now());
        log.info("                                                      ");
        log.info(" __    ___   _      ___   _     ____ _____  ____ ");
    }

}

