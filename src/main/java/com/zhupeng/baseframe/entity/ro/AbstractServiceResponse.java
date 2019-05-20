package com.zhupeng.baseframe.entity.ro;


import com.zhupeng.baseframe.common.enumcommon.ResponseStatus;
import io.swagger.annotations.ApiModelProperty;

/**
 *  controller返回消息抽象类
 *
 * 所有controller的返回都需要实现该接口，需要做校验
 *
 * 再次可以封装一些公共的请求信息,以及用于返回数据日志的记录
 *
 * 目的：用于统一返回信息
 *
 * @author helecong
 *
 */
public abstract class AbstractServiceResponse /*extends AbstractBaseDTO implements IServiceResponse*/ {
    @ApiModelProperty(value="返回消息")
    protected ResponseStatus responseStatus;
    //返回成功标识
    @ApiModelProperty(value="返回成功标识")
    private boolean success = true;
    //返回异常标识，如若存在异常，则返回true
    @ApiModelProperty(value="返返回异常标识")
    private boolean exception = false;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }
    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
    public boolean getSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getException() {
        return exception;
    }
    public void setException(boolean exception) {
        this.exception = exception;
    }

}
