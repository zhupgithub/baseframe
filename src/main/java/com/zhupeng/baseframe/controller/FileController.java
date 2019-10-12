package com.zhupeng.baseframe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@RestController
public class FileController {

    /**
     * springmvc  返回图片到前端主要使用下面三种：
     * 1、返回图片的http地址：使用ftp服务器上的ftp地址。或者OSS的地址，让前端直接去获取相应的图片
     * 2、返回base64格式的图片：String base = Base64.getEncoder().encodeToString(byte[] bytes); 然后直接将这个base字符串返回给前端
     * 3、直接返回二进制的图片，功能如下
     *
     * @param response
     */
    @GetMapping("uploadImg/test")
    public void showImg(HttpServletResponse response){

        response.setContentType("image/jpg");//设置返回图片的类型(image/jpeg/jpg/png/gif/bmp/tiff/svg)

        File image = new File("C:\\Users\\Administrator\\Desktop\\1.jpg");
        try {
            OutputStream os = response.getOutputStream();

            //读取文件(缓存字节流)
            BufferedInputStream bIn = new BufferedInputStream(new FileInputStream(image));
            //写入相应的文件
            BufferedOutputStream bOut = new BufferedOutputStream(os);

            //读取数据
            //一次性取多少字节
            byte[] bytes = new byte[2048];
            //接受读取的内容(n就代表的相关数据，只不过是数字的形式)
            int n = -1;
            //循环取出数据
            while ((n = bIn.read(bytes,0,bytes.length)) != -1) {
                //转换成字符串
                String str = new String(bytes,0,n,"GBK");
                //写入相关文件
                bOut.write(bytes, 0, n);
            }
            //清楚缓存
            bOut.flush();
            //关闭流
            bIn.close();
            bOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
