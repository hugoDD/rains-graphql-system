package com.rains.graphql.arthas.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.rains.graphql.common.exception.ValidationException;
import com.rains.graphql.common.utils.StringUtils;
import org.codehaus.plexus.archiver.tar.TarEntry;
import org.codehaus.plexus.archiver.tar.TarInputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class SshTool {
    /**
     * 获得连接
     *
     * @param ip
     * @param userName
     * @param pwd
     * @return
     */
    public static Connection getConnection(String ip, String userName, String pwd, Integer port) throws Exception {
        Connection connection = new Connection(ip, port);
        boolean blag = false;
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(userName, pwd);
            if (isAuthenticated) {
                blag = true;
            }
            if (isAuthenticated == false) {
                throw new IOException("权限验证失败，请检查账号密码是否正确.");
            }
        } catch (IOException e) {
            throw new Exception("服务器[" + ip + "]无法访问，请检查一下访问权限", e);
        }
        if (blag) {
            return connection;
        } else {
            return null;
        }
    }

    /**
     * 免密登录
     *
     * @param ip       服务器地址
     * @param userName 用户名
     * @param port     端口号
     * @param rsaKey   私钥
     * @param pwd      密码
     * @return
     */
    public static Connection getConnection(String ip, String userName, Integer port, char[] rsaKey, String pwd) throws Exception {
        Connection connection = null;
        if (StringUtils.isBlank(ip)) {
            throw new ValidationException("服务器地址不能为空");
        } else if (StringUtils.isBlank(userName)) {
            userName = "root";
        } else if (null == port) {
            port = 22;
        } else if (null == rsaKey) {
            throw new ValidationException("私钥不能为空");
        }

        try {
            connection = new Connection(ip, port);
            connection.connect();
            /* Authenticate */
            boolean isAuthenticated = connection.authenticateWithPublicKey(userName, rsaKey, pwd);
            if (isAuthenticated == false) {
                throw new Exception("权限验证失败，请检查账号密码是否正确.");
            }
        } catch (Exception e) {
            throw new Exception("服务器[" + ip + "]无法访问，请检查一下访问权限", e);
        }
        return connection;
    }

    /**
     * @param connection 下载文件到本地
     * @param srcFile    要下载的文件
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean download(Connection connection, String srcFile, OutputStream outputStream) {
        boolean flag = false;
        Session sessionSsh = null;
        SCPClient scpClient = null;
        try {
            sessionSsh = connection.openSession();
            scpClient = connection.createSCPClient();
            String filename = srcFile.substring(srcFile.lastIndexOf("/") + 1);
            if (filename.contains(".")) {
                try {
                    scpClient.get(srcFile, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    //是文件夹，打包下载
                    String src = srcFile.substring(0, srcFile.lastIndexOf("/"));
                    String cmdGet = "tar -zcvf " + srcFile + ".tar.gz " + filename;
                    // 执行压缩命令
                    sessionSsh.execCommand("cd " + src + ";" + cmdGet);
                    InputStream stdout = new StreamGobbler(sessionSsh.getStdout());
                    BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                    // cmriots!@#
                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        System.out.println(line);
                    }
                    System.out.println("ExitCode: " + sessionSsh.getExitStatus());
                    // 下载文件
                    scpClient.get(src + "/" + filename + ".tar.gz", outputStream);
                    flag = true;
                }
            } else {
                //是文件夹，打包下载
                String src = srcFile.substring(0, srcFile.lastIndexOf("/"));
                String cmdGet = "tar -zcvf " + srcFile + ".tar.gz " + filename;
                // 执行压缩命令
                sessionSsh.execCommand("cd " + src + ";" + cmdGet);
                InputStream stdout = new StreamGobbler(sessionSsh.getStdout());
                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                // cmriots!@#
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    System.out.println(line);
                }
                System.out.println("ExitCode: " + sessionSsh.getExitStatus());
                // 下载文件
                scpClient.get(src + "/" + filename + ".tar.gz", outputStream);
                //unGzipFile(saveFile+"/"+filename+".tar.gz");
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != sessionSsh) {
                sessionSsh.close();
                sessionSsh = null;
            }
            if (null != scpClient) {
                scpClient = null;
            }
        }
        return flag;
    }

    /**
     * 上传文件到服务端
     *
     * @param connection            ssh连接
     * @param localFile             本地文件
     * @param remoteTargetDirectory 上传服务器目录
     * @param unTarFileName         解压后的目录名
     */
    public static boolean uploadAndUnTar(Connection connection, String localFile, String remoteTargetDirectory, String unTarFileName) {
        boolean result = false;
        Session session = null;
        InputStream stdout = null;
        BufferedReader br = null;
        try {
            SCPClient scpClient = connection.createSCPClient();
            scpClient.put(localFile, remoteTargetDirectory);
            //执行命令
            if (localFile.endsWith(".tar.gz")) {
                session = connection.openSession();
                String filename = localFile.substring(localFile.lastIndexOf("\\") + 1);
                filename = filename.substring(0, filename.indexOf("."));
                if (null == unTarFileName || unTarFileName.trim().equals("")) {
                    unTarFileName = filename;
                }
                String cmdGet = "tar -xzvf " + filename + ".tar.gz " + unTarFileName;
                session.execCommand("cd " + remoteTargetDirectory + ";" + cmdGet);
                stdout = new StreamGobbler(session.getStdout());
                br = new BufferedReader(new InputStreamReader(stdout));
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    System.out.println(line);
                }
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != stdout) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    stdout = null;
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    br = null;
                }
            }
            if (null != session) {
                session.close();
                session = null;
            }
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param connection    ssh
     * @param data          数据
     * @param fileURI       远程服务器地址
     * @param unTarFileName 解压后的目录名
     * @return
     */
    public static boolean uploadAndUnTar(Connection connection, byte[] data, String fileURI, String unTarFileName) throws ValidationException {
        //判断
        if (null == connection) {
            throw new ValidationException("ssh连接失败");
        }
        if (null == data) {
            throw new ValidationException("上传内容为空");
        }
        if (null == fileURI) {
            throw new ValidationException("上传的地址有误");
        }
        Session session = null;
        InputStream stdout = null;
        BufferedReader br = null;
        String filename = fileURI.substring(fileURI.lastIndexOf("/") + 1);
        String filepath = fileURI.substring(0, fileURI.lastIndexOf("/"));
        SCPClient scpClient = null;
        try {
            scpClient = connection.createSCPClient();
            scpClient.put(data, filename, filepath);
            //如果是tar包进行解压
            if (filename.endsWith("tar.gz")) {
                session = connection.openSession();
                if (null == unTarFileName || unTarFileName.trim().equals("")) {
                    unTarFileName = filename.substring(0, filename.lastIndexOf(".tar"));
                }
                String cmdGet = "tar -xzvf " + filename + " " + unTarFileName;
                session.execCommand("cd " + filepath + ";" + cmdGet);
                stdout = new StreamGobbler(session.getStdout());
                br = new BufferedReader(new InputStreamReader(stdout));
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    System.out.println(line);
                }
            }

            return true;
        } catch (IOException e) {
            throw new ValidationException("文件上传失败", e);
        } finally {
            if (null != stdout) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    stdout = null;
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    br = null;
                }
            }
            if (null != session) {
                session.close();
                session = null;
            }
        }
    }

    /**
     * 执行命令
     *
     * @param connection
     * @param cmd
     * @return
     */
    public static SshResponse excuteCmd(Connection connection, String cmd) {
        SshResponse sshResponse = new SshResponse();
        if (null != connection) {
            Session session = null;
            try {
                session = connection.openSession();
                sshResponse = excuteCmd(session, cmd);
            } catch (IOException e) {
                e.printStackTrace();
                sshResponse.setStatus(false);
                sshResponse.setResultMessage("获取session失败");
            } finally {
                if (null != session) {
                    session.close();
                    session = null;
                }
            }
        } else {
            sshResponse.setStatus(false);
            sshResponse.setResultMessage("connection不能为空");
        }
        return sshResponse;
    }

    public static SshResponse excuteCmd(Session session, String cmd) {
        SshResponse result = new SshResponse();
        InputStream stdout = null;
        BufferedReader br = null;
        try {
            //执行命令
            session.execCommand(cmd);
            //正确信息
            stdout = new StreamGobbler(session.getStdout());
            br = new BufferedReader(new InputStreamReader(stdout));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while (null != (line = br.readLine())) {
                sb.append(line + "\n");
            }
            result.setSuccessMsg(sb.toString());
            //错误信息
            stdout = new StreamGobbler(session.getStderr());
            br = new BufferedReader(new InputStreamReader(stdout));
            sb = new StringBuffer();
            while (null != (line = br.readLine())) {
                sb.append(line + "\n");
            }
            result.setErrorMsg(sb.toString());
            result.setStatus(true);
            result.setResultMessage("执行成功");
        } catch (IOException e) {
            e.printStackTrace();
            result.setStatus(false);
            result.setResultMessage("执行失败");
        } finally {
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stdout = null;
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                br = null;
            }
        }
        return result;
    }

    public static boolean checkFile(Connection connection, String remoteFile) {
        boolean result = false;
        if (null == remoteFile || remoteFile.trim().equals("")) {
            return result;
        }
        String cmd = "find /tmp/ -name " + remoteFile;
        SshResponse sshResponse = excuteCmd(connection, cmd);
        String successMsg = sshResponse.getSuccessMsg();
        if (null != successMsg && successMsg.startsWith("/tmp/" + remoteFile)) {
            result = true;
        }
        return result;
    }

    //上传解压
    public static boolean checkFile(Session session, String remoteFile) {
        boolean result = false;
        if (null == remoteFile || remoteFile.trim().equals("")) {
            return result;
        }
        String cmd = "find /tmp/ -name " + remoteFile;
        SshResponse sshResponse = excuteCmd(session, cmd);
        String successMsg = sshResponse.getSuccessMsg();
        if (null != successMsg && successMsg.startsWith("/tmp/" + remoteFile)) {
            result = true;
        }
        return result;
    }

    /**
     * 解压tar.gz 文件
     *
     * @param file      要解压的tar.gz文件对象
     * @param outputDir 要解压到某个指定的目录下
     * @throws IOException
     */
    public static void unTarGz(File file, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        try {
            tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))), 1024 * 2);
            createDirectory(outputDir, null);//创建输出目录
            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    } catch (IOException ex) {
                        throw ex;
                    } finally {

                        if (out != null)
                            out.close();
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
    }


    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }


    public static void main(String[] args) {
        String hostname = "192.168.23.128";
        String username = "root";
        String keyValue = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "Proc-Type: 4,ENCRYPTED\n" +
                "DEK-Info: AES-128-CBC,6139BC862E7D7DB30E034780B947B988\n" +
                "\n" +
                "VAIgVUdtBSSr0g8D9H01HDXcrzq+sIFfTUqUQAdfpSgHn1m5vOTUs78VWt0txVIN\n" +
                "2mS1Mz0RCysA9wmjMplQu6SPr7yBKTipsj2Qx4K3lEmj3hy578rgqFnaH8LMj3UL\n" +
                "yqiDWSIDVIKuGLIhdZgb2XPUdKkAMR/abzQsxtMN1BATGI/jH1JDjIhT+7ILkpom\n" +
                "rUNJFNuFc4oz2tpJC71TmBnVpumXtjMbtM19SbMSWaPzkuUiPEFW4xDwTN++vfej\n" +
                "pmsd7eWciaMKnjJPz9zUG0BPuPzP+pEzEU+At8wfKT/U/jQpS1d8bCwD6vkxkkru\n" +
                "vcolMYUKt3gw6j5eLC2ZJzhnkJFERZm01CWqPcn49Pre2/6WbGqah2ThqnOmc3t5\n" +
                "Ovw9P9VT+Q892eVo/km87KJnv2PxmLtcr8ngPj17+1l0Wsd/Wt8SDW5k5XPOfy6r\n" +
                "5IZ5ww7dF44vUSd0tAlC/4icEtTuXtQM5C9cmqyde4JswGxPVd0uYTFNYosrLdR1\n" +
                "4tU7npLdfm+mu8oW+s3j+RhOdA4/b2F34yC7jRHGatuBEpVuuFgN+rG6N1GjjDVo\n" +
                "jKTGB0deWt0iPqIBguOb1ZaitZ/DHtqCBzKZnUzyVw/JinaKBJZ87onSlyW5mTOn\n" +
                "x787IDWj44GRbZ1QMmVSeVNBRKgIJtYNGyZs577mA2LDDzLr0Ga89zQcENVlgnTm\n" +
                "BD7W6ceabrZ6+5p/DDJDtLceq3E2PsVMFbgFvIDVz08POxGe83aGHk6Qf/4CVkIU\n" +
                "ssYZCexUJpupJV4Hx2m3CpCWYDGZ28f0cXJwlbX/TanyU7DveAF0eSy+sZ1aWSiE\n" +
                "YesfspQFjjwuysyq3eYBP3vIgmk2ZGQflY6o8cB2778I9KhZVzQsWbko2ERRQAMv\n" +
                "PaZ9+pKFjrAQoIYo10SsE2qPI4KNBJgLx3FwmuZskKh4rB0y1NPINPrdsYfi+fHz\n" +
                "Hao6JLbLqyb2XohsvYVrmVRsw/mVUvgWnOZ10fAmaKU+tXT+fyI1Q8I8zsKA5iCL\n" +
                "qvQ5BtvpUg+9JVtsA0HQXeR/Q6upJUPJU6nLDRNdIOh/Gr6WiWEOMsxIq900ugA8\n" +
                "eFOPxAXVrjhPL6i2UNsgtDk9T/ArebWarPgNqjL8IMNn4Sj9EeWHPHuR6W/pmbO2\n" +
                "EI57rWe1TVF3pOikVMQmsCP5wLnDe295Ngo+hcEJnE8oXuQHdhXdKuW1p9u++5it\n" +
                "aicLpOZt2yc7FxkPAGGAH6bRW3MItm84cmtiXZmflQ0DSNwhKFq20UVtdUec7M5x\n" +
                "scRBfjGAFRobEN4UrmxPAWmJ8W42pNmEmbjsI6QjE92jwNZn03T+4hwXRGjqEyqR\n" +
                "Ar7EwJAA9ILrxdD2uhcXogGpdp8cnI7Ttw04Plsks0+by4i7FP0Ee0+SEYwlPs+j\n" +
                "nti1L8mvoanGJxoUC+8IrxkQshYUIZr87xC15WmmZHA9DFR+o9c7uRS5G8Rb/tMg\n" +
                "hqf5SuGb9WEAdLoIHTBn2cRo2FlsvoJP/Okb3IkCM3nJ3Rul1jy9F1a/3s7UR5FT\n" +
                "7wredD45ZYq0V9WabOgbqgvBHtrtMcdxEjE/yk7RpCtmF94IimGVBfey8rAe0ODc\n" +
                "/Leu5Mj2XrZdiezJrk3veFcE5/lqeABFlDroRkBV2F3IKlWlmZ5t/4/yhlQhFc9R\n" +
                "O7/tWErGBUeq0nKaJDPZ6r6f4Ug9byps4qzL0D8PpCFVF6oJOudvnvfMlkW/2eJj\n" +
                "waCxjwArbj974zcrPM/2PGhSbtrYxi1UulULPQdA9bvL0ggfou9nZVUeRtZBkY1A\n" +
                "KuDxgDTNilJQ7vJfvdp09emnr2k8VBCuwtY0enNvYuer1TgxEi3+ErmRyD3jJ1d+\n" +
                "yWDnO4LWVP4rF98eFD/FtI3IZfkrck07ehyzutDm+34M6qcD/0s8eBqrsNSmcOFQ\n" +
                "QcnW7AUpgfJSzn0/VYig2TR6mOo/i9aeNb5VwV2nbiSKnHvPznH04lMeJhqkqsWg\n" +
                "POdDczITAtTgtl4Vm0ofdmaoKXpXUQrS6EZ2f5aZFa06MWvmusOOAA/gib9GOhwZ\n" +
                "vajKLN/fu2RsJZFh3NQslgKEJ0UNvOGFGlsQm+QDNC76MPmPHtuRcfX+iLn6NWkf\n" +
                "Fn3ZxQaLLm5volUBXve3OvWAzPlN0VfD+iGBc8HqrYYVH1rOIJUtwykPDZdMaTGL\n" +
                "LzNVmmtS7sR5+4JSTzwlipEz1i1UUDRw2RW8XixX4EVuG4Ws3Pq+Bld31sNW6Tg+\n" +
                "wRfmwtWFK5TQwLyb8TOW1Vm4/07eQrmOD+2M9XUSTVt2Vod9T3/BHoB4DtzwiTpO\n" +
                "KziR8TGNezjtj+J+XY/S4sooxaE7rVr/gOo4RaYsDUnLATRkJtiKeYlHR8xywZ1G\n" +
                "lR0zGi4O1zA/tCXy612ba2OoyLYhx2OLa3peFaIzn933zkKyPADtq7pFVdTc5oCI\n" +
                "71Fl+128BvPOUHqvnR4LT8P0E3oi5WQ2/061gUpVMqIrfjzfmsqZUyFicV5p0/Mh\n" +
                "dLdTglpcimM3oN2Zm3gXXOf+MY+DNrVX2WIg9TTau01jQln2rNHdoVJk6QwL1aS3\n" +
                "MWCRaFpAUa6jiqCp0bgeIfVtVWft1fPW8Lk6HgQ/1XVpOmFgetHKHx5/qefQLX1f\n" +
                "Ej0BYH/JFhKz5hkEnWL5WDOYINlgWqxVewT68uPneWJ8XikGxBy/DNIIlVtohqiw\n" +
                "H6lIB9hDr55mMUZfNbpUwRo7oWUrSo/PWRsNQE0TpaIZmhw4Zy28d6kKNLYlkPH1\n" +
                "/P3g62U+nsG4tZIclBMZBiy9LSkG0lKhjgghb42PNxtzy6uD5ErZrAEn54vww0Y3\n" +
                "dJ8TkwadXTH48Erwq9WJsKcmcej8BXbybLgWIItUzXLEL5nVcbAYCJf0Fwc68GJI\n" +
                "3v72+9iesZz7/QAfehJhm2jhegzWhm4tOztOrdQExrTzMIWsvyKuONeuegUKyNSi\n" +
                "NnYjNHt8Y8cX1jkIW6QhokmxroB42FVpCotzUyRUuQgepG1feREYh64f/KAFTBsr\n" +
                "khk0fozPeZtBbyCTKBxQMxgsjYODMl6pB1N3zuB+Qq6BtN4u65tY3NCJoswo7BCF\n" +
                "kcC9d+8JSUQhxqo67fieJju+hune3S+e8M5ipoQnYJqNFWM1hiCDBI9ih+n7oTlB\n" +
                "-----END RSA PRIVATE KEY-----\n";
//        File keyfile = new File("C:\\Users\\uc\\.ssh\\id_rsa"); // or "~/.ssh/id_dsa"
        String keyfilePass = "Wr12345678."; // will be ignored if not needed
        try {
            /* Create a connection instance */
            Connection conn = new Connection(hostname);
            /* Now connect */
            conn.connect();
            /* Authenticate */
            boolean isAuthenticated = conn.authenticateWithPublicKey(username, keyValue.toCharArray(), keyfilePass);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            /* Create a session */
            Session sess = conn.openSession();
            sess.execCommand("uname -a && date && uptime && who");
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            System.out.println("Here is some information about the remote host:");
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
            /* Close this session */
            sess.close();
            /* Close the connection */
            conn.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
