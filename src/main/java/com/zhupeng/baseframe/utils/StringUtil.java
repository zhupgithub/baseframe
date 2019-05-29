package com.zhupeng.baseframe.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * 字符串转换成整形
     * @param strInt
     * @return
     */
    public static int toInt(String strInt) {
        if(StringUtils.isBlank(strInt)) {
            return -1;
        }

        try {
            return Integer.valueOf(strInt);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字符串转换成长整形
     * @param strLong
     * @return
     */
    public static long toLong(String strLong) {
        if(StringUtils.isBlank(strLong)) {
            return -1;
        }

        try {
            return Long.valueOf(strLong);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字符串转换成double
     * @param strDouble
     * @return
     */
    public static double toDouble(String strDouble) {
        if(StringUtils.isBlank(strDouble)) {
            return -1;
        }

        try {
            return Double.valueOf(strDouble);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 判断传入的参数中是否有空字符串（null）空白字符串("","  ")
     * @param params
     * @return
     */
    public static boolean hasBlank(String... params) {
        if(params==null || params.length == 0) {
            return true;
        }

        for(String param : params) {
            if(StringUtils.isBlank(param)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断传入的参数中是否有空字符串("")
     * @param params
     * @return
     */
    public static boolean hasEmpty(String... params) {
        if(params==null || params.length == 0) {
            return true;
        }

        for(String param : params) {
            if(StringUtils.isEmpty(param)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 返回16进制的MDS加密串
     *
     * @param str
     *  需要加密的字符串
     * @return
     */
    public static String md5Encrypt(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();

            return toHexString(messageDigest.digest(str.getBytes("UTF-8")));

        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException",e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 把指定的数据转化为16进制格式的字符串
     *
     * @param data
     *            待转化的数据
     * @return 16进制表示的数据
     */
    public static String toHexString(byte[] data) {

        return toHexString(data, 0, data.length);
    }

    /**
     * 把指定的数据转化为16进制格式的字符串， 如toHexString({8,9,12,4},1,3) = "090C"
     *
     * @param data 待转化的数据
     * @param beginIndex  需转化的数据的起始索引
     * @param endIndex 需转化的数据的结束索引
     * @return 16进制表示的数据
     */
    public static String toHexString(byte[] data, int beginIndex, int endIndex) {

        if (data == null || beginIndex < 0)
            return null;
        StringBuilder strBuilder = new StringBuilder();
        for (int i = beginIndex; i < endIndex; i++) {
            strBuilder.append(hexDigits[data[i] >>> 4 & 0xf]);
            strBuilder.append(hexDigits[data[i] & 0xf]);
        }
        return strBuilder.toString();
    }

    /**
     * 根据输入的字符串，生成指定长度的随机字符串
     * @param strPool
     * @param length
     * @return
     */
    public static String randomString(String strPool,int length) {
        if (strPool==null || length < 1) {
            return null;
        }

        Random randGen = new Random();
        char[] numbersAndLetters = (strPool).toCharArray();

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(strPool.length())];
        }
        return new String(randBuffer);
    }

    /**
     * 替换字符串模板中的占位属性
     * @param values
     * @param strTemplate
     * @return
     */
    public static String generateMessageWithTemplate(Map<String,Object> values,String strTemplate){
        String resultMessage = null;
        try {
            Template template = new Template("name", new StringReader(strTemplate),new Configuration());
            StringWriter writer = new StringWriter();
            template.process(values,writer);
            resultMessage = writer.toString();
        } catch (IOException e) {
            logger.error("ZHPStringUtil.[generateMessageWithTemplate] : is IOE exception!!",e);
        } catch (TemplateException e) {
            logger.error("ZHPStringUtil.[generateMessageWithTemplate] : is Template exception!!",e);
        }

        return resultMessage;
    }

    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static String nullToEmpty(String src) {
        if ((src == null) || (src.equalsIgnoreCase("NULL")))
            return "";
        return src;
    }

    public static void main(String[] args) {
        System.out.println(md5Encrypt("123456"));
    }
}

