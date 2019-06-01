package com.zhupeng.baseframe.aop;

import com.alibaba.fastjson.JSON;
import com.zhupeng.baseframe.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;


/**
 * 
 * ClassName: UserCheckAopAdviseDefind 
 * @Description:  该注解用户前后端调试时使用，在需要调试的方法上面加入该注解，方便进行调试
 * @author zhupeng
 * @date 2019年1月15日
 */
@Component
@Aspect
@Slf4j
@Order(1)
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
		printResponseLog(httpServletResponse ,JSON.toJSONString(retVal));
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
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpRequest.getInputStream()));;
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
	 * @param httpServletResponse
	 * @param returnStr
	 */
	private String printResponseLog(HttpServletResponse httpServletResponse, String returnStr) {
		int status = httpServletResponse.getStatus();
		String contentType = httpServletResponse.getContentType();
		String headers = getAllHeader(httpServletResponse);
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
