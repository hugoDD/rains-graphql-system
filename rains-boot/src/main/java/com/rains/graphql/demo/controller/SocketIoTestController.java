package com.rains.graphql.demo.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping(value = "/socketIoTest")
public class SocketIoTestController {

    @Autowired
    private SocketIOServer socketIOServer;

    @RequestMapping(value = "/broadcast")
    public void broadcast() {
        //广播式发送消息
        //向所以订阅了名为text事件的客户端发送消息
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        allClients.parallelStream().forEach(x -> {
            x.sendEvent("text", "Hello socketIo!");
        });
    }

}
