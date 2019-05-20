package com.zhupeng.baseframe.common.enumcommon;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 返回信息状态
 *
 * @author helecong
 */
public class ResponseStatus implements Serializable
{
    public ResponseStatus()
    {

    }

    public ResponseStatus(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    //返回编码
    @ApiModelProperty(value = "返回编码")
    private String code    = "0000";
    //返回信息
    @ApiModelProperty(value = "返回信息")
    private String message = "响应成功";

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
