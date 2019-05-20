package com.zhupeng.baseframe.entity.aop;
import java.util.Date;

/**
 * 应用程序上下文
 * @author helecong
 *
 */
public final class ApplicationContext {
    private String requestParam;  //请求参数
    private String responseBody; //请求体
    private Date requestDate = new Date(); //请求时间
    private Date responseDate; //响应时间

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }


}
