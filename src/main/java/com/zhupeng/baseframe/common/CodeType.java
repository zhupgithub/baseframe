package com.zhupeng.baseframe.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 全局枚举数据
 */
public enum CodeType {
    SYSTEM_ERROR(9999,"系统发生未知异常"),
    INVALID_RETURN_VALUE(9998,"不合法的返回值"),
    INTERFACE_NOT_REALIZE(9997,"接口未实现"),
    TYPE_CONVERSION_ERROR(9996,"类型转换异常"),
    USER_NOT_LOGIN(9995,"用户未登入"),
    USER_NOT_PERMISSION(9994,"用户权限不足"),
    LOG_POST_TO_SERVER_ERROR(9993,"发送日志到服务器异常"),
    DATA_ERROR(9992,"数据库执行异常"),
    VALID_ERROR(9991,"参数验证统一异常"),
    USER_REGISTRATION_SUCCESS(9990,"用户注册成功")
    ;
    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误信息
     */
    private String msg;

    CodeType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CodeType getErrorCodeTypeByCode(int code) {

        for (CodeType codeType : CodeType.values()) {
            if (codeType.getCode() == code) {
                return codeType;
            }
        }

        throw new IllegalArgumentException("未找到错误代码:" + code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
