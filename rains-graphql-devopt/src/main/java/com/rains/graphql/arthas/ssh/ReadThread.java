package com.rains.graphql.arthas.ssh;

import java.io.InputStream;

public abstract class ReadThread implements Runnable {
    public InputStream in;
    //编码
    public String charset = "UTF-8";
    //定义一个flag,来停止线程用
    private boolean isStop = false;

    /**
     * 停止线程
     */
    public void stopThread() {
        this.isStop = true;
    }

    public void run() {
        try {
            //读取数据
            //session 不是空的
            //线程是否停止
            //session是打开的状态
            while (!isStop) {
                //写数据到客户端
                write(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract void write(InputStream in);
}
