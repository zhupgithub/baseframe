package com.zhupeng.baseframe.utils;

import java.security.MessageDigest;

public class MD5Utils {
    /***
     * MD5 加密
     */
    public static String encoder(String str) {
        if (str == null)
            return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            String strTemp;
            for (int i = 0; i < digest.length; i++) {
                strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
                hexString.append(strTemp);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
