package com.zhupeng.baseframe.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
 * ClassName: HttpUtils 
 * @Description: 发送http请求工具类
 * @author zhupeng
 * @date 2018年12月20日
 */
public class HttpUtils {
	private static final String ENCODE_TYPE = "utf-8";

    private static HttpClient httpClient = null;

    private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 
     * @Description: 发送post请求，传入参数为json格式的
     * @param @param URL  请求url地址
     * @param @param jsonRequest json格式的传入参数
     * @param @return   
     * @return String  
     * @throws
     * @author zhupeng
     * @date 2018年12月21日
     */
    public static String postJsonRequest(String URL, String jsonRequest) {
    	
        if (httpClient == null) {
            httpClient = new HttpClient(connectionManager);
            httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ENCODE_TYPE);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
            httpClient.getHttpConnectionManager().getParams().setStaleCheckingEnabled(true);
        }

        PostMethod method = new PostMethod(URL);
        try {
            StringRequestEntity requestEntity = new StringRequestEntity(jsonRequest, "application/json", ENCODE_TYPE);
            method.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(method);

            logger.debug("[HttpUtil.postJsonRequest]:http post execute status '{}'", status);
            if (status != HttpStatus.SC_OK) {
                method.abort();
                return null;
            }

            return readResponse(method.getResponseBodyAsStream());
        } catch (ConnectTimeoutException e) {
            logger.warn("[HttpUtil.postJsonRequest]:timeout to connect target url:{}", URL);
            method.abort();
        } catch (SocketTimeoutException e) {
            logger.warn("[HttpUtil.postJsonRequest]:socket timeout to read the response");
            method.abort();
        } catch (HttpException e) {
            logger.warn("[HttpUtil.postJsonRequest]:connection exception:", e);
            method.abort();
        } catch (IOException e) {
            logger.warn("[HttpUtil.postJsonRequest]:io exception:", e);
            method.abort();
        } finally {
            method.releaseConnection();
            connectionManager.closeIdleConnections(0);
        }

        return null;
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     */
    public static String postRequest(String url, Map<String, String> params) {
        if (httpClient == null) {
            httpClient = new HttpClient(connectionManager);
            httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ENCODE_TYPE);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
            httpClient.getHttpConnectionManager().getParams().setStaleCheckingEnabled(true);
        }

        PostMethod method = new PostMethod(url);
        NameValuePair[] postParams = new NameValuePair[params.size()];
        int index = 0;
        for (Entry<String, String> entry : params.entrySet()) {
            postParams[index] = new NameValuePair(entry.getKey(), entry.getValue());
            index++;
        }

        method.setRequestBody(postParams);
        try {
            int status = httpClient.executeMethod(method);

            logger.debug("[HttpUtil.postRequest]:http post execute status '{}'", status);
            if (status != HttpStatus.SC_OK) {
                logger.error("[HttpUtil.postRequest]:Method failed: " + method.getStatusLine());
                method.abort();
                return null;
            }

            return readResponse(method.getResponseBodyAsStream());
        } catch (ConnectTimeoutException e) {
            logger.warn("[HttpUtil.postRequest]:timeout to connect target url:{}", url);
            method.abort();
        } catch (SocketTimeoutException e) {
            logger.warn("[HttpUtil.postRequest]:socket timeout to read the response");
            method.abort();
        } catch (HttpException e) {
            logger.warn("[HttpUtil.postRequest]:connection exception:", e);
            method.abort();
        } catch (IOException e) {
            logger.warn("[HttpUtil.postRequest]:io exception:", e);
            method.abort();
        } finally {
            method.releaseConnection();
            connectionManager.closeIdleConnections(0);
        }

        return null;
    }

    public static String getRequestByHeadParams(String url, Map<String, String> headParams) {
        if (httpClient == null) {
            httpClient = new HttpClient(connectionManager);
            httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ENCODE_TYPE);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
            httpClient.getHttpConnectionManager().getParams().setStaleCheckingEnabled(true);
        }

        GetMethod method = new GetMethod(url);
        if (headParams != null) {
            for (Entry<String, String> entry : headParams.entrySet()) {
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            int status = httpClient.executeMethod(method);

            logger.debug("[HttpUtil.getRequest]:http get execute status '{}'", status);
            if (status != HttpStatus.SC_OK) {
                logger.error("[HttpUtil.getRequest]:Method failed: " + method.getStatusLine());
                method.abort();
                return null;
            }

            return readResponse(method.getResponseBodyAsStream());
        } catch (ConnectTimeoutException e) {
            logger.warn("[HttpUtil.getRequest]:timeout to connect target url:{}", url);
            method.abort();
        } catch (SocketTimeoutException e) {
            logger.warn("[HttpUtil.getRequest]:socket timeout to read the response");
            method.abort();
        } catch (HttpException e) {
            logger.warn("[HttpUtil.getRequest]:connection exception:", e);
            method.abort();
        } catch (IOException e) {
            logger.warn("[HttpUtil.getRequest]:io exception:", e);
            method.abort();
        } finally {
            method.releaseConnection();
            connectionManager.closeIdleConnections(0);
        }

        return null;
    }

    public static String getRequest(String url, Map<String, String> urlParams) {
        if (httpClient == null) {
            httpClient = new HttpClient(connectionManager);
            httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ENCODE_TYPE);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000);
            httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
            httpClient.getHttpConnectionManager().getParams().setStaleCheckingEnabled(true);
        }

        String urlParam = "";
        if (urlParams != null) {
            for (Entry<String, String> entry : urlParams.entrySet()) {
                urlParam += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        if (StringUtils.isNotBlank(urlParam)) {
            urlParam = urlParam.substring(0, urlParam.length() - 1);
            url += "?" + urlParam;
        }
        logger.info("請求url地址："+url);
        GetMethod method = new GetMethod(url);

        try {
            int status = httpClient.executeMethod(method);
            logger.info("請求返回碼："+status);
            
            logger.debug("[HttpUtil.getRequest]:http get execute status '{}'", status);
            if (status != HttpStatus.SC_OK) {
                logger.error("[HttpUtil.getRequest]:Method failed: " + method.getStatusLine());
                method.abort();
                return null;
            }

            logger.info("請求返回數據："+method.getResponseBodyAsStream());
            return readResponse(method.getResponseBodyAsStream());
        } catch (ConnectTimeoutException e) {
            logger.warn("[HttpUtil.getRequest]:timeout to connect target url:{}", url);
            method.abort();
        } catch (SocketTimeoutException e) {
            logger.warn("[HttpUtil.getRequest]:socket timeout to read the response");
            method.abort();
        } catch (HttpException e) {
            logger.warn("[HttpUtil.getRequest]:connection exception:", e);
            method.abort();
        } catch (IOException e) {
            logger.warn("[HttpUtil.getRequest]:io exception:", e);
            method.abort();
        } finally {
            method.releaseConnection();
            connectionManager.closeIdleConnections(0);
        }

        return null;
    }

    /*
     * read response from the stream
     */
    private static String readResponse(InputStream is) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODE_TYPE));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            logger.warn("io exception:", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.warn("fail to close the input stream:", e);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String url = "http://sdkhttp.eucp.b2m.cn/sdkproxy/regist.action";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cdkey", "0SDK-EBB-0130-JIUMN");
        params.put("password", "503497");

        System.out.println(postRequest(url, params));
    }

}
