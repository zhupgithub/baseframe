package com.zhupeng.baseframe.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * MultipartFile
     *
     * @param multipartFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile , File file){

        try {
            if(file == null){
                String path = multipartFile.getOriginalFilename();//文件的名称（含文件后缀名）

                String prefix = path;
                String suffix = null;

                int lastIndex = path.lastIndexOf(".");
                if(lastIndex > -1){
                    suffix = path.substring(lastIndex); //文件的名称（不含后缀名）
                    prefix = path.substring(0, lastIndex);  //文件的后缀名
                }
                file = File.createTempFile(prefix, suffix);  //这是创建临时文件

                // file.deleteOnExit();   //删除临时文件
            }
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件转化失败");
        }
        return file;
    }

    /**
     *  创建文件，当文件存在时，则不创建；当文件不存在时，则创建
     * @param filePath
     * @return
     */
    public static File createFile(String filePath){

        File file = new File(filePath);
        File parentFile = file.getParentFile();//该文件的文件夹
//        String parentPath =file.getParent();//该文件的文件夹路径

        if(!file.exists()){
            parentFile.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
