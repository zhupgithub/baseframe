package com.zhupeng.baseframe.utils;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Stream2ByteArrayUtil {
    public static byte[] getByteByStream(ServletInputStream inputStream) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024*4];
        int n=0;
        boolean ready =  inputStream.available()>0;
        if(!ready){
            return out.toByteArray();
        }
        while ( (n=inputStream.read(buffer)) !=-1) {
            out.write(buffer,0,n);
        }
        return out.toByteArray();
    }
}
