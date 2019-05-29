package com.zhupeng.baseframe.servicelink;


import java.util.Map;

/**
 * 服务链路信息
 * 该信息应放在请求头中
 * @author zhupeng
 *
 */
public class ServiceLinkInfo {
    // 一下字段用于微服务的链路跟踪，确保发现是哪个日志出问题了
    // 服务链路唯一标识，如果存在则沿用，不存在则新创建
    private String serviceLinkUId;
    // 上层http服务名称
    private ServiceLinkSpan serviceLinkSpan;
    /**
     * 注释，标记链路状态，分以下四种
     *
     * cs - Client Start,表示客户端发起请求
     *
     * sr - Server Receive,表示服务端收到请求
     *
     * ss - Server Send,表示服务端完成处理，并将结果发送给客户端
     *
     * cr - Client Received,表示客户端获取到服务端返回信息
     */
    private String annotation;

    //额外需要添加的信息
    private Map<String,Object> binaryAnnotation;


    public String getServiceLinkUId() {
        return serviceLinkUId;
    }

    public void setServiceLinkUId(String serviceLinkUId) {
        this.serviceLinkUId = serviceLinkUId;
    }

    public ServiceLinkSpan getServiceLinkSpan() {
        return serviceLinkSpan;
    }

    public void setServiceLinkSpan(ServiceLinkSpan serviceLinkSpan) {
        this.serviceLinkSpan = serviceLinkSpan;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Map<String, Object> getBinaryAnnotation() {
        return binaryAnnotation;
    }

    public void setBinaryAnnotation(Map<String, Object> binaryAnnotation) {
        this.binaryAnnotation = binaryAnnotation;
    }

}
