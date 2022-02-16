package com.webserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;

/**
 * 该线程任务负责与指定的客户端完成HTTP交互
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



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
