package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 该线程任务负责与指定的客户端完成HTTP交互
 * 每次HTTP交互都采取一问一答的规则，因此交互由三步来完成:
 * 1:解析请求
 * 2:处理请求
 * 3:发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            /*
                http://localhost:8088/myweb/index123.html
                http://localhost:8088/myweb/index.html
                http://localhost:8088
             */

            //2处理请求
            //例如:浏览器地址栏输入的路径为:http://localhost:8088/myweb/index.html
            //那么解析请求后得到的抽象路径部分uri:/myweb/index.html
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(request, response);

            //3发送响应
            response.response();

            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */


            System.out.println("响应发送完毕!!!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //一次HTTP交互后断开链接(HTTP协议要求)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}






