package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示一个HTTP协议规定的响应内容。
 * 每个响应由三部分构成:
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {
    //状态行相关信息
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

    //响应头相关信息
    //key:响应头的名字 value:响应头对应的值
    private Map<String, String> headers = new HashMap<>();

    //响应正文的相关信息
    private File contentFile;

    private Socket socket;

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    /**
     * 发送响应
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        //3.1发送状态行
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文
        sendContent();
    }

    private void sendStatusLine() throws IOException {
        String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
        println(line);
        System.out.println("发送状态行:" + line);
    }

    private void sendHeaders() throws IOException {
        //遍历headers将所有响应头发送给浏览器
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();//获取响应头的名字
            String value = e.getValue();//获取响应头对应的值
            //Content-Type: text/html
            String line = name + ": " + value;
            println(line);
            System.out.println("响应头:" + line);
        }

        //单独发送回车+换行表示响应头部分发送完毕
        println("");
    }

    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[1024 * 10];
        int len;
        try (
                FileInputStream fis = new FileInputStream(contentFile);
        ) {
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
        System.out.println("响应正文发送完毕!");
    }


    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//发送回车符
        out.write(10);//发送换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }
/**
 * 添加一个要发送的响应头
 */

    public void addHeaders(String name,String value){

    }
}
