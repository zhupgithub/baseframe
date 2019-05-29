package com.zhupeng.baseframe.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhupeng.baseframe.common.ResponseStatus;
import com.zhupeng.baseframe.entity.aop.ApplicationContext;
import com.zhupeng.baseframe.entity.aop.CurrentApplicationContext;
import com.zhupeng.baseframe.entity.aop.RequestWrapper;
import com.zhupeng.baseframe.entity.aop.ResponseWrapper;
import com.zhupeng.baseframe.entity.ro.BaseCommonResponse;
import com.zhupeng.baseframe.exception.BaseRunTimeException;
import com.zhupeng.baseframe.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

@WebFilter(urlPatterns = { "/*" },filterName = "baseControllerFilter")
public class BaseControllerFilter implements Filter{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ObjectMapper mapper = JSONUtils.getObjectMapper();

    private static final String LINE = "----------------------------------------------------";
    private static final String REQUEST_LINE = "--------------------request-------------------------";
    private static final String RESPONSE_LINE = "--------------------response------------------------";
    private ThreadLocal<Long> localId = new ThreadLocal<Long>();
    private static long id = 1;

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        initApplicationContext();
        localId.set(id++);
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        RequestWrapper requestWrapper = null;
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            printRequestLog(requestWrapper);
        }
        // 日志输出
        Object object = null;
        try {

            if (null == requestWrapper) {
                chain.doFilter(servletRequest, servletResponse);
            } else {
                chain.doFilter(requestWrapper, responseWrapper);
            }

        } catch (BaseRunTimeException e) {
            object = this.error(e.getErrorCode(), e.getErrorMsg());
            logger.error("ControllerFilter", e);
        } catch (NestedServletException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof BaseRunTimeException) {
                BaseRunTimeException brte = (BaseRunTimeException) rootCause;
                object = this.error(brte.getErrorCode(), brte.getErrorMsg());
                logger.error("ControllerFilter", e);
            } else {
                object = this.error("9999", "未知异常");
                logger.error("ControllerFilter", e);
            }
        } catch (Throwable e) {
            object = this.error("9999", "未知异常");
            logger.error("ControllerFilter", e);
        }

        if (object != null) {// 存在异常，格式化输出异常
            String returnStr = mapper.writeValueAsString(object);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            printResponseLog(httpResponse.getStatus(), httpResponse.getContentType(), getAllHeader(httpResponse),
                    returnStr);
            PrintWriter writer = httpResponse.getWriter();
            writer.write(returnStr);
            writer.close();
        } else {
            // 输出返回日志
            printResponseLog(responseWrapper);

            httpResponse.setContentLength(-1);// 解决可能在运行的过程中页面只输出一部分
            httpResponse.setCharacterEncoding("UTF-8");
            byte[] data = responseWrapper.getResponseData();
            ServletOutputStream out = httpResponse.getOutputStream();
            out.write(data);
            out.flush();
            out.close();

        }

    }
    private void initApplicationContext() {
        ApplicationContext context = new ApplicationContext();
        CurrentApplicationContext.setApplicationContext(context);
    }

    private String printResponseLog(ResponseWrapper httpResponse) throws IOException {
        return this.printResponseLog(200, httpResponse.getContentType(), getAllHeader(httpResponse),
                new String(httpResponse.getResponseData()));
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
        sb = sb.append("\nID:").append(localId.get());
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
        logger.info("--响应请求--" + sb.toString());
        return sb.toString();
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
        sb = sb.append("\nID:").append(localId.get());
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

        logger.info("--接收到请求--" + sb.toString());
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
     * 错误处理
     *
     * @param errorCode
     * @param errorMsg
     * @return
     */
    private Object error(String errorCode, String errorMsg) {
        BaseCommonResponse res = new BaseCommonResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(errorCode);
        responseStatus.setMessage(errorMsg);
        res.setSuccess(false);
        res.setException(true);
        res.setResponseStatus(responseStatus);
        return res;
    }
}
