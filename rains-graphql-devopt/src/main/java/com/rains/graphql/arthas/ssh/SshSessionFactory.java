package com.rains.graphql.arthas.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.rains.graphql.common.exception.ValidationException;
import com.rains.graphql.common.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * ssh - session  交互管理
 */
public class SshSessionFactory {
    //最大开启session数量
    final static int num = 10;
    public static List<SshSessionEntity> sshPool = new ArrayList<>();

    /**
     * 创建session
     *
     * @param hostname
     * @param username
     * @param password
     * @param webSocketSession websocketsession
     * @return
     * @throws ValidationException
     */


    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, char[] rsa, String sessionId, javax.websocket.Session webSocketSession, Integer xtermWidth, Integer xtermHeight) throws ValidationException {
        SshSessionEntity sshSessionEntity = null;
        if (sshPool.size() >= num) {
            throw new ValidationException("开启连接太多，超过 " + num + " 个");
        } else if (StringUtils.isBlank(hostname)) {
            throw new ValidationException("主机名不能为空");
        } else if (StringUtils.isBlank(username)) {
            throw new ValidationException("用户名不能为空");
        } else if (StringUtils.isBlank(password)) {
            throw new ValidationException("密码不能为空");
        } else {
            try {
                if (null == port) {
                    port = 22;
                }
                Connection connection = null;
                if (rsa != null) {
                    connection = SshTool.getConnection(hostname, username, port, rsa, password);
                } else {
                    connection = SshTool.getConnection(hostname, username, password, port);
                }

                if (connection != null) {
                    //打开连接
                    Session session = connection.openSession();
                    //打开bash
//                    session.requestPTY("bash");
                    if (null == xtermWidth) {
                        xtermWidth = 0;
                    }
                    if (null == xtermHeight) {
                        xtermHeight = 0;
                    }
                    session.requestPTY("xterm", 90, 30, xtermWidth, xtermHeight, null);
                    session.startShell();
                    sshSessionEntity = new SshSessionEntity();
                    sshSessionEntity.setHostname(hostname);
                    sshSessionEntity.setPassword(password);
                    sshSessionEntity.setUsername(username);
                    sshSessionEntity.setConnection(connection);
                    sshSessionEntity.setSession(session);
                    if (StringUtils.isBlank(sessionId)) {
                        sessionId = UUID.randomUUID().toString();
                    }
                    sshSessionEntity.setSessionId(sessionId);
                    //设置输入流
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(session.getStdin(), sshSessionEntity.getCharset()));
                    sshSessionEntity.setBufferedWriter(bufferedWriter);

                    //添加获取返回值线程
                    // 启动多线程，来获取我们运行的结果
                    // 第一个参数 输入流
                    // 第二个参数 输出流，这个直接输出的是控制台
                    ReadThread readThread = null;
                    if (null == webSocketSession) {
                        readThread = new PrintReadThread(session.getStdout(), System.out);
                    } else {
                        readThread = new WebSocketReadThread(session.getStdout(), webSocketSession);
                    }
                    new Thread(readThread).start();

                    sshSessionEntity.setReadThread(readThread);
                    //websocketSessionId
                    sshSessionEntity.setWebSocketSession(webSocketSession);
                    sshPool.add(sshSessionEntity);
                } else {
                    throw new ValidationException("账号或密码错误");
                }
            } catch (Exception e) {
                throw new ValidationException("服务器连接异常", e);
            }
        }
        return sshSessionEntity;
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, char[] rsa, String sessionId, javax.websocket.Session webSocketSession) throws ValidationException {
        return createSession(hostname, username, password, port, rsa, sessionId, webSocketSession, null, null);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, String sessionId, javax.websocket.Session webSocketSession, Integer xtermWidth, Integer xtermHeight) throws ValidationException {
        return createSession(hostname, username, password, port, null, sessionId, webSocketSession, xtermWidth, xtermHeight);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, String sessionId, javax.websocket.Session webSocketSession) throws ValidationException {
        return createSession(hostname, username, password, port, null, sessionId, webSocketSession, null, null);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, char[] rsa, Integer xtermWidth, Integer xtermHeight) throws ValidationException {
        return createSession(hostname, username, password, port, rsa, null, null, xtermWidth, xtermHeight);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, char[] rsa) throws ValidationException {
        return createSession(hostname, username, password, port, rsa, null, null, null, null);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port, Integer xtermWidth, Integer xtermHeight) throws ValidationException {
        return createSession(hostname, username, password, port, null, null, null, xtermWidth, xtermHeight);
    }

    public synchronized static SshSessionEntity createSession(String hostname, String username, String password, Integer port) throws ValidationException {
        return createSession(hostname, username, password, port, null, null, null, null, null);
    }


    /**
     * 获取session
     *
     * @param sessionId 主机
     * @return
     */
    public synchronized static SshSessionEntity getSession(String sessionId) throws ValidationException {
        SshSessionEntity sshSessionEntity = null;
        if (null != sshPool) {
            if (StringUtils.isBlank(sessionId)) {
                throw new ValidationException("sessionId不能为空");
            } else {
                for (SshSessionEntity tmpSshSessionEntity : sshPool) {
                    if (sshSessionEntity.getSessionId().equals(sessionId)) {
                        sshSessionEntity = tmpSshSessionEntity;
                        break;
                    }
                }
            }
        } else {
            throw new ValidationException("连接池为空");
        }
        return sshSessionEntity;
    }

    public synchronized static List<SshSessionEntity> closeSession(SshSessionEntity sshSessionEntity) {
        if (null != sshSessionEntity) {
            return closeSession(sshSessionEntity.getSessionId());
        } else {
            return sshPool;
        }
    }

    /**
     * 关闭session
     *
     * @param sessionId
     * @return
     * @throws IOException
     */
    public synchronized static List<SshSessionEntity> closeSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            throw new ValidationException("sessionId不能为空");
        } else {
            if (sshPool != null && sshPool.size() > 0) {
                // 获取迭代器
                Iterator<SshSessionEntity> it = sshPool.iterator();
                while (it.hasNext()) {
                    SshSessionEntity tmpSshSessionEntity = it.next();
                    if (tmpSshSessionEntity.getSessionId().equals(sessionId)) {
                        if (null != tmpSshSessionEntity.getConnection()) {
                            tmpSshSessionEntity.getConnection().close();
                        }
                        if (null != tmpSshSessionEntity.getSession()) {
                            tmpSshSessionEntity.getSession().close();
                        }
                        if (null != tmpSshSessionEntity.getReadThread()) {
                            tmpSshSessionEntity.getReadThread().stopThread();
                        }
                        if (null != tmpSshSessionEntity.getBufferedWriter()) {
                            try {
                                tmpSshSessionEntity.getBufferedWriter().close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        it.remove();
                        sshPool.remove(tmpSshSessionEntity);
                        if (null == sshPool || sshPool.size() <= 0) {
                            break;
                        }
                    }
                }
            }
        }
        return sshPool;
    }

    /**
     * 关闭所有session
     */
    public synchronized static void closeAllSession() {
        if (sshPool != null && sshPool.size() > 0) {
            for (SshSessionEntity tmpSshSessionEntity : sshPool) {
                closeSession(tmpSshSessionEntity.getSessionId());
            }
        }
    }
}
