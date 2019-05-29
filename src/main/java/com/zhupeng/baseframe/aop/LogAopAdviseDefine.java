package com.zhupeng.baseframe.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Component
@Aspect
@Slf4j
@Order(1)
public class LogAopAdviseDefine {
	private static final Logger logger = LoggerFactory.getLogger(LogAopAdviseDefine.class);
	
	@Pointcut("!execution(* com.zhupeng.baseframe.aop.*.*(..)) && !execution(* com.zhupeng.baseframe.config.*.*(..)) && execution(* com.zhupeng.baseframe..*.*(..))")
	private void pointcut(){}
	
	
	@Before("pointcut()")
	public void logRecordOnMethodBefore(JoinPoint joinPonit) throws ClassNotFoundException, NotFoundException{
		
//		joinPonit.getSignature().toLongString();
//		String classType = joinPonit.getTarget().getClass().getName();
//        Class<?> clazz = Class.forName(classType);
//        String clazzName = clazz.getName();
//        String methodName = joinPonit.getSignature().getName(); //获取方法名称
//        Object[] args = joinPonit.getArgs();//参数
//          //获取参数名称和值
//        Map<String,Object > nameAndArgs = ReflectUtils.getFieldsName(this.getClass(), clazzName, methodName,args);
//        logger.info("调用{}方法，参数是：{}",methodName,nameAndArgs.toString());


		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String className = joinPonit.getTarget().getClass().getName();
		String methodName = joinPonit.getSignature().getName();
		Map<String, String[]> paramMaps = httpServletRequest.getParameterMap();
		String paramMap = JSONObject.toJSONString(paramMaps);
		logger.info("请求request ==> url:[{}], class:[{}], method:[{}], paramters:[{}]", new Object[]{
				httpServletRequest.getRequestURL(), className, methodName, paramMap});
	}
	

	@AfterReturning(pointcut = "pointcut()" , returning = "retVal")
	public void logRecordOnMethodAfter(JoinPoint joinPonit,Object retVal){
		String className = joinPonit.getTarget().getClass().getName();
		String methodName = joinPonit.getSignature().getName();
		String jsonResult = "";
		if(retVal != null) {
			jsonResult = JSON.toJSONString(retVal);
		}
		logger.info("返回response ==>  class:[" + className + "], method:[" + methodName + "], paramters:[" + jsonResult + "]");
	}
	
	@AfterThrowing(pointcut = "pointcut()" ,throwing = "e")
	public void logRecordOnMethodException(JoinPoint joinPonit,Exception e){
		logger.info("调用{}方法，发生异常，异常信息是：{}",joinPonit.getSignature().toShortString(),e.getMessage());
	}
}
