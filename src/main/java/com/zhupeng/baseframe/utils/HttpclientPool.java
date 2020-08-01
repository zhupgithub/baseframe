package com.zhupeng.baseframe.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 创建连接池并且获取连接，用于http或者HTTPS都可以
 */
@Slf4j
public class HttpclientPool {
    private static CloseableHttpClient httpClient = null;

    static {
        try {
            initHttpClient();
        } catch (Exception e) {
            log.error("创建http客户端发生异常" , e);
        }
    }


    private static void initHttpClient() throws Exception {
        //用来读取公钥做验证加密数据传输的
        TrustManager manager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{manager}, null);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
            // 验证host的那个，允许访问返回true
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 总连接池数量
        connectionManager.setMaxTotal(100);
        // 可为每个域名设置单独的连接池数量
//        connectionManager.setMaxPerRoute( 1);
        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000)
                .setSocketTimeout(3000).build();
        // 重试处理器，StandardHttpRequestRetryHandler这个是官方提供的，看了下感觉比较挫，很多错误不能重试，可自己实现HttpRequestRetryHandler接口去做
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();

        // 服务端假设关闭了连接，对客户端是不透明的，HttpClient为了缓解这一问题，在某个连接使用前会检测这个连接是否过时，如果过时则连接失效，但是这种做法会为每个请求
        // 增加一定额外开销，因此有一个定时任务专门回收长时间不活动而被判定为失效的连接，可以某种程度上解决这个问题
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 关闭失效连接并从连接池中移除
                    connectionManager.closeExpiredConnections();
                    // 关闭20秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
                    connectionManager.closeIdleConnections(20, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 0 , 1000 * 5);
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}
