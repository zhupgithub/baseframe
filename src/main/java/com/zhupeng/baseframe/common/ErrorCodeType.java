package com.zhupeng.baseframe.common;

import org.apache.commons.lang3.StringUtils;

public enum ErrorCodeType {
    SYSTEM_ERROR("9999","系统发生未知异常"),
    INVALID_RETURN_VALUE("9998","不合法的返回值"),
    INTERFACE_NOT_REALIZE("9997","接口未实现"),
    TYPE_CONVERSION_ERROR("9996","类型转换异常"),
    USER_NOT_LOGIN("9995","用户未登入"),
    USER_NOT_PERMISSION("9994","用户权限不足"),
    LOG_POST_TO_SERVER_ERROR("9994","发送日志到服务器异常"),
    DATA_ERROR("9993","数据库执行异常"),
    VALID_ERROR("9992","参数验证统一异常")
    ;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;

    ErrorCodeType(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static ErrorCodeType getErrorCodeTypeByCode(String errorCode) {
        if (StringUtils.isEmpty(errorCode)) {
            throw new NullPointerException("错误代码为空");
        }

        for (ErrorCodeType errorCodeType : ErrorCodeType.values()) {
            if (errorCodeType.getErrorCode().equals(errorCode)) {
                return errorCodeType;
            }
        }

        throw new IllegalArgumentException("未找到错误代码:" + errorCode);
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

}
