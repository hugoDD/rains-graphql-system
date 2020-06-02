//package com.rains.graphql.demo.component;
//
//import com.corundumstudio.socketio.AckRequest;
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.annotation.OnConnect;
//import com.corundumstudio.socketio.annotation.OnDisconnect;
//import com.corundumstudio.socketio.annotation.OnEvent;
//import io.netty.util.internal.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentSkipListSet;
//
///**
// * 消息处理类实现
// */
//@Component
//public class SocketIoHandler {
//    @Autowired
//    private SocketIOServer socketIOServer;
//
//    private static ConcurrentHashMap<String, ConcurrentSkipListSet> roomMap = new ConcurrentHashMap();
//
//    //监听客户端连接
//    @OnConnect
//    public void onConnect(SocketIOClient client) {
//        System.out.println(client.getRemoteAddress() + "-------------------------" + "客户端已连接");
//
//        UUID sessionId = client.getSessionId();
//        String roomId = client.getHandshakeData().getSingleUrlParam("roomId");
//        if (StringUtil.isNullOrEmpty(roomId)) {
//            return;
//        }
//
//        ConcurrentSkipListSet<UUID> concurrentSkipListSet = roomMap.get(roomId);
//        if (concurrentSkipListSet == null || concurrentSkipListSet.isEmpty()) {
//            concurrentSkipListSet = new ConcurrentSkipListSet();
//        }
//        concurrentSkipListSet.add(sessionId);
//        roomMap.putIfAbsent(roomId, concurrentSkipListSet);
//    }
//
//
//    //监听客户端断开
//    @OnDisconnect
//    public void onDisconnect(SocketIOClient client) {
//        System.out.println(client.getRemoteAddress() + "-------------------------" + "客户端已断开连接");
//
//        UUID sessionId = client.getSessionId();
//        String roomId = client.getHandshakeData().getSingleUrlParam("roomId");
//        if (StringUtil.isNullOrEmpty(roomId)) {
//            return;
//        }
//        ConcurrentSkipListSet<UUID> concurrentSkipListSet = roomMap.get(roomId);
//        if (concurrentSkipListSet == null || concurrentSkipListSet.isEmpty()) {
//            return;
//        }
//        concurrentSkipListSet.remove(sessionId);
//        roomMap.putIfAbsent(roomId, concurrentSkipListSet);
//    }
//
//
//    //监听名为roomMessageSending的请求事件
//    @OnEvent(value = "roomMessageSending")
//    public void onEvent(SocketIOClient client, AckRequest ackRequest, String data) {
//        String roomId = client.getHandshakeData().getSingleUrlParam("roomId");
//        if (StringUtil.isNullOrEmpty(roomId)) {
//            return;
//        }
//        ConcurrentSkipListSet<UUID> concurrentSkipListSet = roomMap.get(roomId);
//        if (concurrentSkipListSet == null || concurrentSkipListSet.isEmpty()) {
//            return;
//        }
//        concurrentSkipListSet.forEach(x -> socketIOServer.getClient(x).sendEvent(roomId, data));
//    }
//
//}
//
