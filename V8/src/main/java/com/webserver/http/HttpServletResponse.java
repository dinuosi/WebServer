package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServletResponse {
    //状态行信息
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

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
        System.out.println("发送状态行"+line);
    }

    private void sendHeaders() throws IOException {
        String line;
        line = "Content-Type: text/html";
        println(line);

        line = "Content-Length: " + contentFile.length();
        println(line);

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
}
