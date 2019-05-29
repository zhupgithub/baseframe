package com.zhupeng.baseframe.entity.ro;

import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 统一结果返回对象
 * @author  zhupeng
 * code : 200 访问成功
 *        400 访问失败
 */
@Data
public class ResponseResult implements Serializable {

    private int code; //响应码

    private String message; //响应提示语

    private Object data;  //返回数据

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult() {
    }

    public ResponseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult success(int code,String message){
        ResponseResult responseResult = new ResponseResult(code,message);
        return responseResult;
    }

    public static ResponseResult error(int code,String message){
        ResponseResult responseResult = new ResponseResult(code,message);
        return responseResult;
    }

    public static ResponseResult success(){
        ResponseResult responseResult = new ResponseResult(200,"成功");
        return responseResult;
    }

    public static ResponseResult error(){
        ResponseResult responseResult = new ResponseResult(400,"失败");
        return responseResult;
    }
}
