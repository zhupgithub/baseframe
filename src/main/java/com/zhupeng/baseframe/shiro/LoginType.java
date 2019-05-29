package com.zhupeng.baseframe.shiro;

/**
 * 登录方式的枚举类，存放我们所有的登录方式
 */
public enum  LoginType {
    /**
     * 通用
     */
    COMMON("common_realm"),
    /**
     * 用户密码登录
     */
    USER_PASSWORD("user_password_realm"),
    /**
     * 手机验证码登录
     */
    USER_PHONE("user_phone_realm");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
