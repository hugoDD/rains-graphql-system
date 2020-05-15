package com.rains.graphql.arthas.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.BufferedWriter;
import java.io.IOException;


public class SshSessionEntity {
    private String charset = "UTF-8";
    //sessionId
    private String sessionId;
    //机器IP
    private String hostname;
    //用户名
    private String username;
    //登陆密码
    private String password;
    //xterm模式宽度
    private Integer xtermWidth;
    //xterm模式高度
    private Integer XtermHeight;
    //ssh connection
    private Connection connection;
    //ssh session
    private Session session;
    //返回的读取线
    private ReadThread readThread;
    //输出流
    private BufferedWriter bufferedWriter;
    //webSocketSession
    private javax.websocket.Session webSocketSession;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getXtermWidth() {
        return xtermWidth;
    }

    public void setXtermWidth(Integer xtermWidth) {
        this.xtermWidth = xtermWidth;
    }

    public Integer getXtermHeight() {
        return XtermHeight;
    }

    public void setXtermHeight(Integer xtermHeight) {
        XtermHeight = xtermHeight;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ReadThread getReadThread() {
        return readThread;
    }

    public void setReadThread(ReadThread readThread) {
        this.readThread = readThread;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public javax.websocket.Session getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(javax.websocket.Session webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public void write(String cmd) throws IOException {
        bufferedWriter.write(cmd);
        bufferedWriter.flush();
    }
}
