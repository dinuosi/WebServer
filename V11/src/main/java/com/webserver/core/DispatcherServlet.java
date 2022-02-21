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
    static{
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

    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        System.out.println("请求路径:"+path);
        //去static目录下根据用户请求的抽象路径定位下面的文件
        File file = new File(staticDir,path);
        if(file.isFile()){//实际存在的文件
            response.setContentFile(file);
        }else{//1:文件不存在  2:是一个目录
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir,"/root/404.html");
            response.setContentFile(file);
        }
    }
}
