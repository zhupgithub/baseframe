package com.zhupeng.baseframe.utils;


import java.util.UUID;

/**
 * UUID工具类
 */
public class UUIDUtils {
    /**
     *
     * 通过jdk自带的uuid生成器生成36位的uuid；
     */
    public static String getUUID() {
        // 使用JDK自带的UUID生成器
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

