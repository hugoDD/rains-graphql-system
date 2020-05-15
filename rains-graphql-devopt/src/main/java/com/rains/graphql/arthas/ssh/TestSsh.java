package com.rains.graphql.arthas.ssh;

import java.util.Scanner;

public class TestSsh {

    /**
     * 进行交互
     *
     * @param sshSessionEntity
     * @return
     */
    public String send(SshSessionEntity sshSessionEntity) {
        String result = "";
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String commondStr = scanner.nextLine();
                // 输出流
                sshSessionEntity.write(commondStr + "\n");
                if (commondStr.equals("exit")) {
                    scanner.close();
                    SshSessionFactory.closeSession(sshSessionEntity.getSessionId());
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            SshSessionFactory.closeSession(sshSessionEntity.getSessionId());
        }
        return result;
    }



    /**
     * 连接linux系统
     *
     * @param args
     */
    public static void main(String[] args) {
        String hostname = "192.168.23.128";
        String username = "root";
        String password = "reywong";
        SshSessionEntity sshSessionEntity = SshSessionFactory.createSession(hostname, username, password,22);
//        try {
//            sshSessionEntity.write("ls -l \n");
//        } catch (IOException e) {
//        } finally {
//            SshSessionFactory.closeSession(sshSessionEntity.getSessionId());
//        }
        TestSsh ssh = new TestSsh();
        ssh.send(sshSessionEntity);

    }
}
