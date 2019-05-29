package com.zhupeng.baseframe.entity.aop;


import org.springframework.core.NamedThreadLocal;

/**
 * 当前应用数据
 * @author zhupeng
 *
 */
public class CurrentApplicationContext {
    private static final ThreadLocal<ApplicationContext> CURRENT_APPLICATION_CONTEXT_THREAD_LOCAL = new NamedThreadLocal<ApplicationContext>("zhupeng");

    public static void setApplicationContext(ApplicationContext context){
        CURRENT_APPLICATION_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static ApplicationContext getApplicationContext(){
        ApplicationContext applicationContext = CURRENT_APPLICATION_CONTEXT_THREAD_LOCAL.get();
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }
}
