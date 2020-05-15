package com.rains.graphql.arthas.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class PrintReadThread extends ReadThread {
    private PrintStream out;

    /**
     * @param in  输入流，获取的输入
     * @param out 输出流
     */
    public PrintReadThread(InputStream in, PrintStream out) {
        super();
        this.in = in;
        this.out = out;
    }

    @Override
    void write(InputStream in) {
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
                out.print(new String(sb.toString().getBytes("ISO-8859-1"), charset));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
