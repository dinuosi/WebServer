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
            String path = request.getUri();
            System.out.println("请求路径:" + path);


            //临时测试:将resource目录中static/myweb/index.html响应给浏览器
            /*
                实际开发中，我们常用的相对路径都是类的加载路径。对应的写法:
                类名.class.getClassLoader().getResource("./")
                这里的"./"当前目录指的就是类加载路径的开始目录。它的实际位置
                JVM理解的就是当前类的包名指定中最上级包的上一层。
                例如下面的代码中，当前类ClientHandler指定的包:
                package com.webserver.core;
                那么包的最上级就是com，因此类加载路径的开始目录就是com的上级目录
                实际就是项目的target/classes这个目录了。
                maven项目编译后会将src/main/java目录和src/main/resource目录
                最终合并到target/classes中。
             */
            /*
                File(File parent,String sub)
                阅读文档，理解该构造方法

                提示:static目录是确定存在的目录
                我们是要找这个目录下的内容是否存在
             */
            File staticDir = new File(
                    ClientHandler.class.getClassLoader().getResource(
                            "./static"
                    ).toURI()
            );
            //去static目录下根据用户请求的抽象路径定位下面的文件
            File file = new File(staticDir, path);
            if (file.isFile()) {//实际存在的文件
                response.setContentFile(file);
            } else {//1:文件不存在  2:是一个目录
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "/root/404.html");
                response.setContentFile(file);
            }
            //3发送响应
            response.response();

            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */


            System.out.println("响应发送完毕!!!!");
        } catch (IOException | URISyntaxException e) {
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






