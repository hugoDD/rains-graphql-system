package com.rains.graphql.common.handler;

import org.springframework.web.socket.*;

public class MyHandler implements WebSocketHandler {
    // 连接继开处理
    @Override
    public void afterConnectionClosed(WebSocketSession arg0, CloseStatus arg1) throws Exception {
        // TODO Auto-generated method stub

        System.out.println("Connection closed..." + arg0.getRemoteAddress().toString());

    }

    // 连接建立处理
    @Override
    public void afterConnectionEstablished(WebSocketSession arg0) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Connection established..." + arg0.getRemoteAddress().toString());
    }

    // 接收、发送信息处理
    @Override
    public void handleMessage(WebSocketSession arg0, WebSocketMessage<?> arg1) throws Exception {
        // TODO Auto-generated method stub
        try {
            System.out.println("Req: " + arg1.getPayload());
            // 发送信息
            TextMessage returnMessage = new TextMessage(arg1.getPayload() + " received at server");
            arg0.sendMessage(returnMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 错误处理（客户端突然关闭等接收到的错误）
    @Override
    public void handleTransportError(WebSocketSession arg0, Throwable arg1) throws Exception {
        // TODO Auto-generated method stub
        if (arg0.isOpen()) {
            arg0.close();
        }
        System.out.println(arg1.toString());
        System.out.println("WS connection error,close...");
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }
}
