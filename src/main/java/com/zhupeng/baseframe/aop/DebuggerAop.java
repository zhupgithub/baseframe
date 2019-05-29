package com.zhupeng.baseframe.aop;

import com.alibaba.fastjson.JSON;
import com.zhupeng.baseframe.entity.aop.ApplicationContext;
import com.zhupeng.baseframe.entity.aop.CurrentApplicationContext;
import com.zhupeng.baseframe.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;


/**
 * 
 * ClassName: UserCheckAopAdviseDefind 
 * @Description: 判断用户登入是否有效
 * @author zhupeng
 * @date 2019年1月15日
 */
@Component
@Aspect
@Slf4j
@Order(2)
public class DebuggerAop {

	private static final String LINE = "----------------------------------------------------";
	private static final String REQUEST_LINE = "--------------------request-------------------------";
	private static final String RESPONSE_LINE = "--------------------response------------------------";
	@Pointcut("@annotation(com.zhupeng.baseframe.annotation.DebuggingLogAnnotation)")
	public void debuggerAop(){}


	@Before("debuggerAop()")
	public void printRequest(){
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		try {
			printRequestLog(httpServletRequest);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterReturning(pointcut = "debuggerAop()" , returning = "retVal")
	public void printResponse(JoinPoint joinPonit,Object retVal){
		HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		printResponseLog(httpServletResponse.getStatus(), httpServletResponse.getContentType(), getAllHeader(httpServletResponse),
				JSON.toJSONString(retVal));
	}

	//@Around("pointcut()")
	public Object checkUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//		ResponseResult responseResult = new ResponseResult();
//		String classType = proceedingJoinPoint.getTarget().getClass().getName();
//        Class<?> clazz = Class.forName(classType);
//        String clazzName = clazz.getName();
//        String methodName = proceedingJoinPoint.getSignature().getName(); //获取方法名称
//        Object[] args = proceedingJoinPoint.getArgs();//参数
//          //获取参数名称和值
//        Map<String,Object > nameAndArgs = ReflectUtils.getFieldsName(this.getClass(), clazzName, methodName,args);
//        logger.info("调用{}方法，参数是：{}",methodName,nameAndArgs.toString());
//
//		HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		logger.info("请求类型："+request.getContentType()+"   请求方式："+request.getMethod());
//
//		String phone = request.getParameter("phone");
//
////		phone = RedisUtil.getValueByKey(phone);
////		if(phone == null){
////			responseResult.setCode(999);
////			responseResult.setMessage("亲，请先登入");
////			return responseResult;
////		}
//        Object result =  proceedingJoinPoint.proceed();
////        RedisUtil.setValueAndKey(phone, phone,10L,TimeUnit.MINUTES);
//        return result;
		return null;
	}

	/**
	 * 输入请求日志
	 *
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String printRequestLog(HttpServletRequest httpRequest) throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(httpRequest.getRequestURI());
		sb.append("\n" + REQUEST_LINE);
		sb = sb.append("\nRemoteAddr:").append(httpRequest.getRemoteAddr());
		sb = sb.append("\nAddress:").append(httpRequest.getRequestURL());
		sb = sb.append("\nEncoding:").append(httpRequest.getCharacterEncoding());
		sb = sb.append("\nHttpMethod:").append(httpRequest.getMethod());
		sb = sb.append("\nContentType:").append(httpRequest.getContentType());
		sb = sb.append("\nHeaders:").append(getAllHeader(httpRequest));

		Map map = httpRequest.getParameterMap();
		BufferedReader bufferedReader = httpRequest.getReader();
		String line;
		StringBuilder sb1 = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			sb1.append(line);
		}
		line = sb1.toString();
		if (map == null) {
			sb.append("\nPayload-param:").append("");
		} else {
			sb.append("\nPayload-param:").append(JSONUtils.getJsonStr(map));
		}
		if (line.length() > 1500) {
			sb.append("\nPayload-body:").append("body大于1500个字符，不显示");
		} else {
			sb.append("\nPayload-body:").append(line);
		}

		log.info("--接收到请求--" + sb.toString());
		return sb.toString();
	}

	/**
	 * 输出返回消息
	 *
	 * @param status
	 * @param contentType
	 * @param headers
	 * @param returnStr
	 */
	private String printResponseLog(int status, String contentType, String headers, String returnStr) {
		ApplicationContext applicationContext = CurrentApplicationContext.getApplicationContext();
		StringBuilder sb = new StringBuilder();
		sb = sb.append("\n" + RESPONSE_LINE);
		sb = sb.append("\nResponse-Code:").append(status);
		sb = sb.append("\nContent-Type:").append(contentType);
		sb = sb.append("\nHeaders:").append(headers);
		if (returnStr.length() > 40000) {
			sb = sb.append("\nPayload:").append("响应大于40000个字符，不显示");
		} else {
			sb = sb.append("\nPayload:").append(returnStr);
		}
		sb = sb.append("\n" + LINE);
		Date requestDate = applicationContext.getRequestDate();
		Date responseDate = new Date();
		long time = responseDate.getTime() - requestDate.getTime();
		sb.append("\nTotal time:").append(time).append("ms");
		log.info("--响应请求--" + sb.toString());
		return sb.toString();
	}
	/**
	 * 获取所有的请求头信息
	 *
	 * @param httpResponse
	 * @return
	 */
	private String getAllHeader(HttpServletResponse httpResponse) {
		StringBuilder sb = new StringBuilder();
		Collection<String> headerNames = httpResponse.getHeaderNames();
		for (String handerName : headerNames) {
			sb.append(handerName).append("=[").append(httpResponse.getHeader(handerName)).append("], ");

		}
		return sb.toString();
	}
	/**
	 * 获取所有的请求头信息
	 *
	 * @param httpRequest
	 * @return
	 */
	private String getAllHeader(HttpServletRequest httpRequest) {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> e1 = httpRequest.getHeaderNames();
		while (e1.hasMoreElements()) {
			String headerName = (String) e1.nextElement();
			sb.append(headerName).append("=[").append(httpRequest.getHeader(headerName)).append("], ");
		}

		return sb.toString();
	}
}
