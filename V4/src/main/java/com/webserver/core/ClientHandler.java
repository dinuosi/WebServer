package com.webserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 该线程任务负责与指定的客户端完成HTTP交互
 * 每次HTTP交互都采取一问一答的规则，因此交互由散步来完成：
 * 1：解析请求
 * 2：处理请求
 * 3：发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            String line = readLine();
            System.out.println(line);

            //请求行的相关信息
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议

            //请求行内容拆分出来并分别赋值给三个变量
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
            // http://localhost:8088/index.html


            System.out.println("method:" + method);
            System.out.println("uri:" + uri);
            System.out.println("protocol:" + protocol);

            Map<String,String> headers = new HashMap<>();
            //1.2解析消息头
            while (true) {
                line = readLine();
                if(line.isEmpty()){
                    break;
                }
                System.out.println("消息头：" + line);
                //将消息头的名字和值以key,value形式存入headers这个Map中
                data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        int d;
        char pre = 'a', cur = 'a';
        while ((d = in.read()) != -1) {
            cur = (char) d;
            if (pre == 13 & cur == 10) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }


}
