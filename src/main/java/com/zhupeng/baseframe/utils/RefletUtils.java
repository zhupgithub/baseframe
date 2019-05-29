package com.zhupeng.baseframe.utils;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author helecong
 *
 */
@SuppressWarnings("rawtypes")
public class RefletUtils {
    private static Map<Class, MethodAccess> methodMap = new HashMap<Class, MethodAccess>();

    private static Map<String, Integer> methodIndexMap = new HashMap<String, Integer>();

    private static Map<Class, List<String>> fieldMap = new HashMap<Class, List<String>>();

    /**
     * 创建对象
     * @param clazz
     * @return
     */
    public static <T> T createClass(Class<T> clazz){
        try {
            T t = clazz.newInstance();
            return t;
        } catch (InstantiationException e ) {
            throw new RuntimeException(e);
        }catch (IllegalAccessException e1){
            throw new RuntimeException(e1);
        }
    }

    /**
     * 获取访问对象
     * @param clazz
     * @return
     */
    public static MethodAccess getMethodAccess(Class clazz){
        MethodAccess descMethodAccess = methodMap.get(clazz);
        if (descMethodAccess == null) {
            descMethodAccess = cache(clazz);
        }
        return descMethodAccess;
    }

    /**
     * 获取属性字段列表
     * @param clazz
     * @return
     */
    public static List<String> getFieldList(Class clazz){
        List<String> list = fieldMap.get(clazz);
        if(list == null){
            cache(clazz);
            list = fieldMap.get(clazz);
        }
        return list;
    }

    /**
     * 执行字段的 get方法
     * @param object
     * @param methodAccess
     * @param field
     * @return
     */
    public static Object fieldGet(Object object,MethodAccess methodAccess,String field){
        String getKey = object.getClass().getName() + "." + "get" + field;
        Integer getIndex = methodIndexMap.get(getKey);
        if(getIndex == null){
            return null;
        }
        String methodName = "get" + field;
        return methodAccess.invoke(object, methodName );
//		return methodAccess.invoke(object, getIndex);
    }

    /**
     * 执行字段的set方法
     * @param object
     * @param methodAccess
     * @param field
     * @param vlaue
     * @return
     */
    public static Object fieldSet(Object object,MethodAccess methodAccess,String field,Object vlaue){
        String setKey = object.getClass().getName() + "." + "set" + field;
        Integer setIndex = methodIndexMap.get(setKey);
        if(setIndex == null){
            return null;
        }
        String methodName = "set" + field;
        return methodAccess.invoke(object, methodName ,vlaue);
//		return methodAccess.invoke(object, setIndex.intValue(), vlaue);
    }

    // 单例模式
    private static MethodAccess cache(Class clazz) {
        synchronized (clazz) {
            MethodAccess methodAccess = MethodAccess.get(clazz);
            Field[] fields = clazz.getDeclaredFields();
            List<String> listTemp = new ArrayList<String>(fields.length);
			/*for (Field field : fields) {
				if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) { // 是否是私有的，是否是静态的
					// 非公共私有变量
					String fieldName = StringUtils.capitalize(field.getName()); // 获取属性名称
					int getIndex = methodAccess.getIndex("get" + fieldName); // 获取get方法的下标
					int setIndex = methodAccess.getIndex("set" + fieldName); // 获取set方法的下标
					methodIndexMap.put(clazz.getName() + "." + "get" + fieldName, getIndex); // 将类名get方法名，方法下标注册到map中
					methodIndexMap.put(clazz.getName() + "." + "set" + fieldName, setIndex); // 将类名set方法名，方法下标注册到map中
					fieldList.add(fieldName); // 将属性名称放入集合里
				}
			}*/
            // 利用递归方法获取父类及其非Object父类的所有属性
            List<String> fieldList = recursion(clazz,listTemp,clazz.getName());
            fieldMap.put(clazz, fieldList); // 将类名，属性名称注册到map中
            methodMap.put(clazz, methodAccess);
            return methodAccess;
        }
    }

    // 递归方法获取所有的父类及其属性
    private static List<String> recursion(Class clazz,List<String> list,String baseClassName){
        //List<String> recursion = temp.getRecursion();
        MethodAccess methodAccess = MethodAccess.get(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) { // 是否是私有的，是否是静态的
                // 非公共私有变量
                String fieldName = StringUtils.capitalize(field.getName()); // 获取属性名称
                int getIndex = methodAccess.getIndex("get" + fieldName); // 获取get方法的下标
                int setIndex = methodAccess.getIndex("set" + fieldName); // 获取set方法的下标
                methodIndexMap.put(baseClassName + "." + "get" + fieldName, getIndex); // 将类名get方法名，方法下标注册到map中
                methodIndexMap.put(baseClassName + "." + "set" + fieldName, setIndex); // 将类名set方法名，方法下标注册到map中
                list.add(fieldName); // 将属性名称放入集合里
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass != Object.class) {
            recursion(superclass, list,baseClassName);
        }
        return list;
    }

}