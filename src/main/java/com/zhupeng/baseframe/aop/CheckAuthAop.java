package com.zhupeng.baseframe.aop;


import com.zhupeng.baseframe.annotation.CheckAuthAnnotation;
import com.zhupeng.baseframe.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

@Component
@Aspect
@Slf4j
@Order(4)
public class CheckAuthAop{

    @Autowired
    protected RedisUtil redisUtil;

    @Pointcut("@annotation(com.zhupeng.baseframe.annotation.CheckAuthAnnotation) || " +
            " @within(com.zhupeng.baseframe.annotation.CheckAuthAnnotation)")
    public void checkAuthAop(){}

    @Before("checkAuthAop()")
    public void checkAuth(JoinPoint joinPonit) throws ClassNotFoundException, NoSuchMethodException {
        joinPonit.getSignature().toLongString();
        String classType = joinPonit.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String methodName = joinPonit.getSignature().getName(); //获取方法名称
        Method [] methods = clazz.getMethods();
        CheckAuthAnnotation checkAuthAnnotation = null;
        Method method = null;

        for(Method methodTemp : methods){
            if(methodTemp.getName().equals(methodName)){
                method = methodTemp;
                break;
            }
        }
        //判断方法上是否存在相应注解
        boolean isCheckAuthAnnotationExistOnMethod = method.isAnnotationPresent(CheckAuthAnnotation.class);
        if(isCheckAuthAnnotationExistOnMethod){
            checkAuthAnnotation = method.getAnnotation(CheckAuthAnnotation.class);
        }else{
            //获取类上的注解
            checkAuthAnnotation = clazz.getAnnotation(CheckAuthAnnotation.class);
        }
        //进行权限操作
        doCheck(checkAuthAnnotation);
    }


    private void doCheck(CheckAuthAnnotation checkAuthAnnotation){
        if(checkAuthAnnotation == null){
            return;
        }

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(checkAuthAnnotation.checkAuth()){
            checkAuth(httpServletRequest);
        }
        if(checkAuthAnnotation.checkLogin()){
            checkLogin(httpServletRequest);
        }
        if(checkAuthAnnotation.checkDeviceOwner()){
            checkDeviceOwner(httpServletRequest);
        }
        if(checkAuthAnnotation.recordAuditLog()){
            recordAuditLog(httpServletRequest);
        }
    }

    private void checkAuth(HttpServletRequest request) {

    }

    private void checkLogin(HttpServletRequest request) {

    }

    private void checkDeviceOwner(HttpServletRequest request){
        Long deviceId = Long.parseLong((((HashMap<String,Object>)
                request.getAttribute("org.springframework.web.servlet.View.pathVariables")).get("deviceId").toString()));

        Object deviceJson = redisUtil.getValueByKey(String.valueOf(deviceId));

    }

    private void recordAuditLog(HttpServletRequest request){

    }
}
