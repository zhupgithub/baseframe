package com.zhupeng.baseframe.annotation;

import java.lang.annotation.*;

/**
 * @desc 该注解用户前后端调试时使用，在需要调试的方法上面加入该注解，方便进行调试
 * @author zhupeng
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DebuggingLogAnnotation {
    //注解的属性
    String name() default "rose";

    String value() default "jack";

}
