package com.zhupeng.baseframe.utils;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectMapper mapperIgnore = new ObjectMapper();

    private JSONUtils(){}

    public static ObjectMapper getObjectMapper(){
        return mapper;
    }

    /**
     * 获取 getJsonStr
     * 主要功能，将对象转换为json格式的字符串
     * @param obj
     * @return
     */
    public static String getJsonStr(Object obj){
        String writeValueAsString = null;
        try {
            writeValueAsString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return writeValueAsString;
    }

    /**
     * 将json数据封装成知道对象
     * @param jsonStr
     * @param c
     * @return
     */
    public static <T> T getObjectByResString(String jsonStr,Class<T> c){
        T t = null;
        try {
            t = mapper.readValue(jsonStr, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
    /**
     * 将json数据封装成知道对象
     * @param jsonStr
     * @param c
     * @param ignore 是否忽略多于字段,true 表示忽略，可以比bean的字段多,false 表示不可以
     * @return
     */
    public static <T> T getObjectByResString(String jsonStr,Class<T> c,boolean ignore){
        T t = null;
        try {
            mapperIgnore.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !ignore);
            t = mapperIgnore.readValue(jsonStr, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Map jsonToMap(String jsonStr){
        Map readValue = null;
        try {
            readValue = mapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return readValue;
    }

    public static <T>List<T> json2List(String json,Class<T> beanClass){
        return json2List(json, beanClass, false);
    }
    public static <T>List<T> json2List(String json,Class<T> beanClass,boolean ignore){
        mapperIgnore.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !ignore);
        JavaType constructParametricType = mapperIgnore.getTypeFactory().constructParametricType(List.class,beanClass);
        try {
            List<T> list = (List<T>)mapperIgnore.readValue(json, constructParametricType);
            return list;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

/**
     * map 转成bean
     * @param tClass
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Class<T> tClass , Map<String , Object> map){
        String json = JSONObject.toJSONString(map);

        System.out.println("解析前的json字符串：" + json);
        return JSON.parseObject(json, tClass);
    }
}
