package com.zhupeng.baseframe.annotation;

import java.lang.annotation.*;

/**
 * 权限检查的注解，该注解可以使用在类上和方法上，当同时存在时，以方法上为主
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckAuthAnnotation {
    //是否需要检查操作设备的权限，默认是需要
    boolean checkDeviceOwner() default true;

    //是否需要token，默认是需要
    boolean checkAuth() default true;

    //是否需要登入，默认是需要
    boolean checkLogin() default true;

    //是否需要记录到ES中，默认是需要
    boolean recordAuditLog() default true;
}
