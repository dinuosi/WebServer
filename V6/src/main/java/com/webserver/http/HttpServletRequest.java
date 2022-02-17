package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示HTTP协议规定的客户端发过来的一个请求内容
 * 每个请求由三部分构成：
 * 请求航，消息头，消息正文
 */
public class HttpServletRequest {
    //请求行的相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议
    private Socket socket;
    //消息头相关消息
    private Map<String, String> headers = new HashMap<>();

    /*
        实例化请求对象的过程中也是解析的过程
     */
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        parseRequestLine();//1.1解析请求行
        parseHeaders();//1.2解析消息头
        parseContent();//1.3解析消息正文
    }

    /**
     * 解释请求行
     */
    private void parseRequestLine() throws IOException {
        String line = readLine();
        System.out.println(line);
        //请求行内容拆分出来并分别赋值给三个变量
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];

        // http://localhost:8088/index.html

        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);
    }

    /**
     * 解释消息头
     */
    private void parseHeaders() throws IOException {
        while (true) {
            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println("消息头：" + line);
            //将消息头的名字和值以key,value形式存入headers这个Map中
            String[] data = line.split(":\\s");
            headers.put(data[0], data[1]);
        }
        System.out.println("headers:" + headers);
    }

    /**
     * 解释消息正文
     */
    private void parseContent() {
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

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
    public String getHeader(String name){
        return headers.get(name);
    }
}
