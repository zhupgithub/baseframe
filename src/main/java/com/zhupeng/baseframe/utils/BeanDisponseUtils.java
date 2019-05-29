package com.zhupeng.baseframe.utils;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象处理工具类
 * @author zhupeng
 *
 */
public class BeanDisponseUtils {

    public static void copyProperties(Object desc, Object orgi) {
        MethodAccess descMethodAccess = RefletUtils.getMethodAccess(desc.getClass());
        MethodAccess orgiMethodAccess = RefletUtils.getMethodAccess(orgi.getClass());
        List<String> fieldList = RefletUtils.getFieldList(orgi.getClass());
        for (String field : fieldList) {
            Object value = RefletUtils.fieldGet(orgi, orgiMethodAccess, field);
            if(value == null){
                continue;
            }
            RefletUtils.fieldSet(desc, descMethodAccess, field, value);
        }
    }

    /**
     * object 转 map 无异常
     * @param obj
     * @return
     */
    public static Map<String, Object> parseObjectToMapNoException(Object obj, boolean ignoreNull) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = parseObjectToMap(obj,ignoreNull);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * object 转 map 有异常
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseObjectToMap(Object obj,boolean ignoreNull) throws Exception {
        if(obj == null)
            return new HashMap<String, Object>();

        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            if((value == null || StringUtils.isBlank(value.toString()))&& ignoreNull){
                continue;
            }
            map.put(key, value);
        }

        return map;
    }
    /**
     * object 转 map 有异常
     * 默认不忽略空
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseObjectToMap(Object obj) {
        return parseObjectToMapNoException(obj, true);
    }

}

