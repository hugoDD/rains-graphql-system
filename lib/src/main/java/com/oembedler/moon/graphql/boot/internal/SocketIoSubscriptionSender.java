package com.oembedler.moon.graphql.boot.internal;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SocketIoSubscriptionSender {

    private final ObjectMapper objectMapper;

    SocketIoSubscriptionSender(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void send(SocketIOClient client, Object payload) {
        System.out.println("sessionId:"+client.getSessionId());
        try {
            System.out.println("data:" + objectMapper.writeValueAsString(payload));
        }catch (Exception e){
            e.getMessage();
        }
            client.sendEvent("subMessage",payload);
           //Session session.getBasicRemote().sendText(objectMapper.writeValueAsString(payload));

    }
}
