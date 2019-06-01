package com.zhupeng.baseframe.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhupeng.baseframe.utils.RefletUtils;
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
@Order(2)
public class LogAopAdvise {

	@Pointcut("!execution(* com.zhupeng.baseframe.aop.*.*(..)) && !execution(* com.zhupeng.baseframe.config.*.*(..)) " +
            " && execution(* com.zhupeng.baseframe..*.*(..))")
	private void pointcut(){}
	
	
	@Before("pointcut()")
	public void logRecordOnMethodBefore(JoinPoint joinPonit) throws ClassNotFoundException, NotFoundException{
		
		joinPonit.getSignature().toLongString();
		String classType = joinPonit.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String className = clazz.getName();
        String methodName = joinPonit.getSignature().getName(); //获取方法名称
          //获取参数名称和值
		Map<String, String[]> paramMaps = null;
		String url = null;
		if(RequestContextHolder.getRequestAttributes() != null){
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

			paramMaps = httpServletRequest.getParameterMap();
			url = httpServletRequest.getRequestURL().toString();
		}

		String paramMap = JSONObject.toJSONString(paramMaps);
		log.info("请求request ==> url:[{}], class:[{}], method:[{}], paramters:[{}]", new Object[]{
				url, className, methodName, paramMap});
	}
	

	@AfterReturning(pointcut = "pointcut()" , returning = "retVal")
	public void logRecordOnMethodAfter(JoinPoint joinPonit,Object retVal){
		String className = joinPonit.getTarget().getClass().getName();
		String methodName = joinPonit.getSignature().getName();
		String jsonResult = "";
		if(retVal != null) {
			jsonResult = JSON.toJSONString(retVal);
		}
		log.info("返回response ==>  class:[" + className + "], method:[" + methodName + "], paramters:[" + jsonResult + "]");
	}
	
	@AfterThrowing(pointcut = "pointcut()" ,throwing = "e")
	public void logRecordOnMethodException(JoinPoint joinPonit,Exception e){
        e.printStackTrace();
		log.info("调用{}方法，发生异常，异常信息是：{}",joinPonit.getSignature().toShortString(),e.getMessage());
	}
}
