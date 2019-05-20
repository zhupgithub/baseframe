package com.zhupeng.baseframe.exception;

import com.zhupeng.baseframe.common.enumcommon.ErrorCodeType;

/**
 * 所有自定义异常都需要继承这个异常类
 */
public class BaseRunTimeException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String errorCode = "9999";

    /**
     * 错误信息
     */
    private String errorMsg = "系统发生未知异常";
    /**
     * 错误代码类型
     */
    private ErrorCodeType errorCodeType;

    public BaseRunTimeException() {
        super();
    }

    public BaseRunTimeException(ErrorCodeType errorCodeType) {
        this(errorCodeType.getErrorCode(),errorCodeType.getErrorMsg());
        this.errorCodeType = errorCodeType;
    }

    public BaseRunTimeException(String errorCode, String errorMsg) {
        super("ErrorCode:"+errorCode+",ErrorMsg:"+errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseRunTimeException(Throwable cause) {
        super(cause);
        if(cause instanceof BaseRunTimeException){
            BaseRunTimeException e1 = (BaseRunTimeException) cause;
            this.errorCode = e1.getErrorCode();
            this.errorMsg = e1.getErrorMsg();
        }
        // TODO Auto-generated constructor stub
    }

    public BaseRunTimeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public BaseRunTimeException(ErrorCodeType errorCodeType,Throwable cause) {
        super("ErrorCode:"+errorCodeType.getErrorCode()+",ErrorMsg:"+errorCodeType.getErrorMsg(),cause);
        // TODO Auto-generated constructor stub
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ErrorCodeType getErrorCodeType() {
        return errorCodeType;
    }

    public void setErrorCodeType(ErrorCodeType errorCodeType) {
        this.errorCodeType = errorCodeType;
    }

}
