package com.zhupeng.baseframe.servicelink;


/**
 * 服务链路 基本单元
 * @author zhupeng
 *
 */
public class ServiceLinkSpan {
    //由1开始，每次发送到下一阶段，加1
    private int spanId;
    //父服务的名称，首次为空，添加服务名加本服务的端口号
    private String parentServiceName;
    //端口号
    private String parentServiceHost;
    //Ip
    private String parentServiceIp;
    //发送请求的时间搓
    private long sendTime;

    public int getSpanId() {
        return spanId;
    }
    public void setSpanId(int spanId) {
        this.spanId = spanId;
    }
    public String getParentServiceName() {
        return parentServiceName;
    }
    public void setParentServiceName(String parentServiceName) {
        this.parentServiceName = parentServiceName;
    }
    public String getParentServiceHost() {
        return parentServiceHost;
    }
    public void setParentServiceHost(String parentServiceHost) {
        this.parentServiceHost = parentServiceHost;
    }
    public String getParentServiceIp() {
        return parentServiceIp;
    }
    public void setParentServiceIp(String parentServiceIp) {
        this.parentServiceIp = parentServiceIp;
    }
    public long getSendTime() {
        return sendTime;
    }
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
