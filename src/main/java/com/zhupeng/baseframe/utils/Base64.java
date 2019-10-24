package com.zhupeng.baseframe.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {

    /**
     * 将数据进行base64加密
     * @param str
     * @return
     */
    public static String encrypt(String str) throws UnsupportedEncodingException {
        byte[] data = str.getBytes("UTF-8");
        return new BASE64Encoder().encode(data);
    }

    /**
     * base64 解密
     * @param data
     * @return
     * @throws IOException
     */
    public static String decrypt(String data) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(data) , "UTF-8");
    }

    public static void main(String[] args) throws IOException {
        String data = "123456adsaadsad阿道夫7890";

        String result = Base64.encrypt(data);
        System.out.println(" 使用Base64编码的结果： " + result);

        String result2 = Base64.decrypt(result);
        System.out.println("使用Base64解码的结果：" + result2);

    }
}
