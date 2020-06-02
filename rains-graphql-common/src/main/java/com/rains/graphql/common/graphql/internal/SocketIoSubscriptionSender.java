package com.rains.graphql.common.graphql.internal;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SocketIoSubscriptionSender {

    private final ObjectMapper objectMapper;

    SocketIoSubscriptionSender(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void send(SocketIOClient client, Object payload) {
        if(log.isDebugEnabled()){
            try {
                log.debug("sessionId:{},data:{}",client.getSessionId(), objectMapper.writeValueAsString(payload));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(),e);
            }
        }

        client.sendEvent("subMessage",payload);
           //Session session.getBasicRemote().sendText(objectMapper.writeValueAsString(payload));

    }
}
