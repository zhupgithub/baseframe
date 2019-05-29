package com.zhupeng.baseframe.config;

import com.zhupeng.baseframe.shiro.*;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author admin
 * @email admin@rsitf.com
 * @date 2017-04-20 18:33
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        return sessionManager;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<Realm>();

//        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);

        // 统一角色权限控制realm
        realms.add(authorizingRealm());
        // 用户密码登录realm
        realms.add(userPasswordRealm());
        // 用户手机号验证码登录realm
        realms.add(userPhoneRealm());

        securityManager.setRealms(realms);

        return securityManager;
    }

    @Bean
    public AuthorizingRealm authorizingRealm(){
        AuthorizationRealm authorizationRealm = new AuthorizationRealm();
        authorizationRealm.setName(LoginType.COMMON.getType());

        return authorizationRealm;
    }

    /**
     * 密码登录realm
     *
     * @return
     */
    @Bean
    public UserPasswordRealm userPasswordRealm() {
        UserPasswordRealm userPasswordRealm = new UserPasswordRealm();
        userPasswordRealm.setName(LoginType.USER_PASSWORD.getType());
        // 自定义的密码校验器
//        userPasswordRealm.setCredentialsMatcher(credentialsMatcher());
        return userPasswordRealm;
    }

    /**
     * 手机号验证码登录realm
     * @return
     */
    @Bean
    public UserPhoneRealm userPhoneRealm(){
        UserPhoneRealm userPhoneRealm = new UserPhoneRealm();
        userPhoneRealm.setName(LoginType.USER_PHONE.getType());

        return userPhoneRealm;
    }

//    @Bean
//    public CredentialsMatcher credentialsMatcher() {
//        return new CredentialsMatcher();
//    }


    /**
     * 自定义的Realm管理，主要针对多realm
     */
    @Bean("myModularRealmAuthenticator")
    public MyModularRealmAuthenticator modularRealmAuthenticator() {
        MyModularRealmAuthenticator customizedModularRealmAuthenticator = new MyModularRealmAuthenticator();
        // 设置realm判断条件
        customizedModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

        return customizedModularRealmAuthenticator;
    }
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login.html");
        //shiroFilter.setSuccessUrl("/index.html");
        shiroFilter.setUnauthorizedUrl("/");

        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        filterMap.put("/public/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/api/**", "anon");

        //swagger配置
        filterMap.put("/swagger**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-resources/configuration/ui", "anon");
        filterMap.put("/userController/add", "anon");


        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
