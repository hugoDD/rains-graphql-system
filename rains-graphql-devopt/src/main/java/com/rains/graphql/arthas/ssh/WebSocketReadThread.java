package com.rains.graphql.arthas.ssh;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStream;

public class WebSocketReadThread extends ReadThread {
    //用于输出数据
    private Session session;

    public WebSocketReadThread(InputStream in, Session session) {
        super();
        this.in = in;
        this.session = session;
    }

    /**
     * 写数据到web控制界面
     *
     * @param in
     */
    public void write(InputStream in) {
        try {
            //定义一个缓存
            //一个UDP 的用户数据报的数据字段长度为8192字节
            byte[] buff = new byte[8192];
            int len = 0;
            StringBuffer sb = new StringBuffer();
            while ((len = in.read(buff)) > 0) {
                //设定从0 开始
                sb.setLength(0);
                //读取数组里面的数据，进行补码
                for (int i = 0; i < len; i++) {
                    //进行补码操作
                    char c = (char) (buff[i] & 0xff);
                    sb.append(c);
                }
                //写数据到服务器端
                session.getBasicRemote().sendText(new String(sb.toString().getBytes("ISO-8859-1"), charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
