package com.webserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;

/**
 * 该线程任务负责与指定的客户端完成HTTP交互
 * 每次HTTP交互都采取一问一答的规则，因此交互由散步来完成：
 * 1：解析请求
 * 2：处理请求
 * 3：发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            StringBuilder builder = new StringBuilder();
            int d;
            char pre='a',cur = 'a';
            while ((d=in.read())!=-1){
                cur =(char) d;
                if (pre==1&cur==10){
                    break;
                }
                builder.append(cur);
                pre=cur;
            }
            String line = builder.toString();
            System.out.println(line);

            //请求行的相关信息
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议

            //请求行内容拆分出来并分别赋值给三个变量
            String[] a = line.split("\\s");
            method = a[0];
            uri = a[1];
            protocol = a[2];


            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
