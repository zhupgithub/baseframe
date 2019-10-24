package com.zhupeng.baseframe.utils;


import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhupeng.baseframe.entity.co.user.LoginUserCo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONTokener;

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

    public static void main(String[] args) {
        LoginUserCo loginUserCo = new LoginUserCo();
        loginUserCo.setPassword("aaa");
        loginUserCo.setUsername("zhuepng");
        loginUserCo.setRememberMe(true);

        Date now = new Date();
        Integer i = new Integer(1);

        System.out.println(getJsonStr(now));
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
     * 递归解析json字符串，将数组数据存入list集合中，将json对象存入map中
     * @param json 需要解析的json字符串
     */
//    public static Object parseJson(String json) {
//        if(StringUtils.isBlank(json)){
//            return json;
//        }
//        Object typeObject = new JSONTokener(json).nextValue();
//        Object result = json;
//        if (typeObject instanceof org.json.JSONArray) {
//            result = new LinkedList<Object>();
//            for (Object param : (org.json.JSONArray) typeObject) {
//                if (!(param instanceof org.json.JSONArray) && !(param instanceof org.json.JSONObject)) {
//                    ((LinkedList) result).add(param);
//                } else {
//                    String temp;
//                    if (param instanceof org.json.JSONArray) {
//                        temp = ((org.json.JSONArray) param).toString();
//                    } else {
//                        temp = ((org.json.JSONObject) param).toString();
//                    }
//                    Object resultTemp = parseJson(temp);
//                    ((LinkedList) result).add(resultTemp);
//                }
//            }
//        }
//        if (typeObject instanceof org.json.JSONObject) {
//            result = new HashMap<String, Object>();
//            for (String key : ((org.json.JSONObject) typeObject).keySet()) {
//                Object value = ((org.json.JSONObject) typeObject).get(key);
//                if (!(value instanceof org.json.JSONArray) && !(value instanceof org.json.JSONObject)) {
//                    ((HashMap) result).put(key, value);
//                } else {
//                    String temp;
//                    if (value instanceof org.json.JSONArray) {
//                        temp = ((org.json.JSONArray) value).toString();
//                    } else {
//                        temp = ((org.json.JSONObject) value).toString();
//                    }
//                    Object resultTemp = parseJson(temp);
//                    ((HashMap) result).put(key, resultTemp);
//                }
//            }
//        }
//        return result;
//    }

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

    /***
     * 判断字符串是否为合法的json
     * @param json
     * @return
     */
    public static boolean isLegalJson(String json){
        try {
            JSONObject ob = JSONObject.parseObject(json);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /***
     * 判断对象是否为合法的json
     * @param json
     * @return
     */
    public static boolean isLegalJson(Object json){
        return json instanceof JSONArray || json instanceof JSONObject;
    }

    /**
     * 将对象转成json对象，且含类名
     * User{username,password} 转化后 {"user":{"username":"zhu","password":"123"}}
     *
     * @param objects
     * @return
     */
    public static Object toJSONSwap(Object ... objects){
        StringBuffer buffer = new StringBuffer();
        if(ArrayUtils.isEmpty(objects)){
            return null;
        }
        buffer.append("{");
        for(int i = 0 ; i < objects.length ; i++){
            Object object = objects[i];
            if(i > 0){
                buffer.append(",");
            }
            //获得类名并且将第一个字母转为小写
            int index = object.getClass().getName().lastIndexOf(".") + 1;
            String bean = object.getClass().getName().substring(index);
            bean = bean.substring(0, 1).toLowerCase() + bean.substring(1);

            buffer.append("\"").append(bean).append("\" :").append(JSONObject.toJSONString(object))
                    .append("");

        }
        buffer.append("}");

        System.out.println(buffer.toString());
        return JSONObject.parse(buffer.toString());
    }

}
