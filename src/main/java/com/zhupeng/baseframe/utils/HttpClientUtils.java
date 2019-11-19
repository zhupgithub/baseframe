package com.zhupeng.baseframe.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP客户端工具类
 *
 * @author Chen Jianliang
 * @since 1.0
 * @since 2019/7/31
 */
public class HttpClientUtils {
    public static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static final String CONTENT_KEY = "content";
    public static final String STATUS_CODE_KEY = "statusCode";

    public static Map<String, Object> get(String url) {
        return get(httpclient, url);
    }

    public static Map<String, Object> get(CloseableHttpClient httpclient, String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        String backContent = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // start 读取整个页面内容
                InputStream is = entity.getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
                // end 读取整个页面内容
                backContent = buffer.toString();
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap(backContent, response.getStatusLine().getStatusCode());
    }

    public static Map<String, Object> post(String url, List<NameValuePair> nvps) {
        return post(httpclient, url, nvps);
    }

    public static Map<String, Object> post(CloseableHttpClient httpclient, String url, List<NameValuePair> nvps) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String backContent = null;
        try {
            if (nvps != null)
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // start 读取整个页面内容
                InputStream is = entity.getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
                if (buffer.length() > 2) {
                    // end 读取整个页面内容
                    backContent = buffer.delete(buffer.length() - 2, buffer.length()).toString();
                }
                // System.out.println(backContent);
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap(backContent, response.getStatusLine().getStatusCode());
    }

    public static Map<String, Object> post(String url, String json) {
        return post(httpclient, url, json);
    }

    public static Map<String, Object> post(CloseableHttpClient httpclient, String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String backContent = null;

        try {
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-type", "application/json");
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // start 读取整个页面内容
                InputStream is = entity.getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
                if (buffer.length() > 2) {
                    // end 读取整个页面内容
                    backContent = buffer.delete(buffer.length() - 2, buffer.length()).toString();
                }
                // System.out.println(backContent);
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap(backContent, response.getStatusLine().getStatusCode());
    }


    public static Map<String, Object> resultMap(String backContent, int statusCode) {
        Map<String, Object> map = new HashMap<>();
        map.put(CONTENT_KEY, backContent);
        map.put(STATUS_CODE_KEY, statusCode);
        return map;
    }

    public static void main(String[] args) {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("");

        get(urlBuffer.toString());
    }

}

