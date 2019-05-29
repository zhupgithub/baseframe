package com.zhupeng.baseframe.entity.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 基础传输 抽象类
 *
 * 目的：统一toString输出，利用反射
 *
 * @author zhupeng
 *
 */
public class AbstractBaseDTO {
    public static final long serialVersionUID = 1L;

    /**
     * 利用反射返回字符串
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
