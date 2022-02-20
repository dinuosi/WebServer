package com.webserver.http;

import com.webserver.core.ClientHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示HTTP协议规定的客户端发送过来的一个请求内容。
 * 每个请求由三部分构成:
 * 请求行，消息头，消息正文
 */
public class HttpServletRequest {
    //请求行的相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();

    private Socket socket;
    /**
     * 实例化请求对象的过程也是解析的过程
     */
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;
        //1.1解析请求行
        parseRequestLine();
        //1.2解析消息头
        parseHeaders();
        //1.3解析消息正文
        parseContent();

    }

    /**
     *  解析请求行
     */
    private void parseRequestLine() throws IOException {
        String line = readLine();
        System.out.println(line);
        //将请求行内容拆分出来并分别赋值给三个变量
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];

        //测试路径:http://localhost:8088/myweb/index.html
        System.out.println("method:"+method);//method:GET
        System.out.println("uri:"+uri);//uri:/myweb/index.html
        System.out.println("protocol:"+protocol);//protocol:HTTP/1.1
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
        while(true) {
            String line = readLine();
            if(line.isEmpty()){//若读取的字符串为空串，说明单独读取了回车+换行
                break;
            }
            System.out.println("消息头:" + line);
            //将消息头的名字和值以key，value形式存入headers这个Map中
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent(){}


    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        int d;
        char pre='a',cur='a';//pre上一次读取的字符  cur本次读取到的字符
        while((d = in.read())!=-1){
            cur = (char)d;//将本地读取的字节转换为字符赋值给cur
            if(pre==13&cur==10){//是否连续读取到了回车+换行
                break;
            }
            builder.append(cur);//将本次读取的字符拼接到StringBuilder中
            pre = cur;//在进行下一个字符读取前将本地读取的字符记录为上次读取的字符
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

    /**
     * 根据给定的消息头的名字获取对应的值
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
}
