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
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
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
