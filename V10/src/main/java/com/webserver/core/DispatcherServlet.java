package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 处理请求的环节
 */
public class DispatcherServlet {
    //表示sources下的static目录，实际运行时是编译后target/classes下的static目录。
    private static File staticDir;
    static {
        try {
            staticDir = new File(
                    ClientHandler.class.getClassLoader().getResource(
                            "./static"
                    ).toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void service(HttpServletRequest request, HttpServletResponse response) {
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
    }
}
