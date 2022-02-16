package com.webserver.core;

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

    }
}
