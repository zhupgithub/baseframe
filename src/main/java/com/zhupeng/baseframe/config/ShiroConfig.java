package com.zhupeng.baseframe.config;

import com.zhupeng.baseframe.shiro.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * ClassName: ShiroConfig
 * @Description: Shiro配置类
 * @author zhupeng
 * @date 2019年1月14日
 */
@Configuration
@Order(1)
public class ShiroConfig {

    //注入异常处理类
  /* TODO   zhupeng start @Bean
    public MyExceptionResolver myExceptionResolver() {
        return new MyExceptionResolver();
    }
    TODO   zhupeng  end*/


    @Bean   //FIXME 将securityManager设置到shirFilter到中
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //FIXME start  将自定义的filter加入到shiro中
        //验证码过滤器
//        Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
//        KaptchaFilter kaptchaFilter = new KaptchaFilter();
//        filtersMap.put("kaptchaFilter", kaptchaFilter);
//        //实现自己规则roles,这是为了实现or的效果
//        //RoleFilter roleFilter = new RoleFilter();
//        //filtersMap.put("roles", roleFilter);
//        shiroFilterFactoryBean.setFilters(filtersMap);
        //FIXME end  将自定义的filter加入到shiro中



        Map<String,String> map = new HashMap<String, String>();
        map.put("/**/test", "anon");  //测试接口不需要权限
      //swagger配置
        map.put("/swagger**", "anon");
        map.put("/v2/api-docs", "anon");
        map.put("/swagger-resources/configuration/ui", "anon");
        map.put("/docs.html" , "anon");

        map.put("/error.html", "anon");
        map.put("/login.html","anon");
        map.put("register.html","anon");

        //登出
        map.put("/userController/logout","logout");
        map.put("/userController/loginUser","anon");
        map.put("/userController/register","anon");
        map.put("/userController/Captcha.jpg", "anon");//图片验证码(kaptcha框架)
//   TODO  自定义过滤器     map.put("/user/loginUser", "kaptchaFilter");


        /*
         *TODO  开发复杂功能的建议
        //当实在需要多种角色展示不同界面和功能时，可以这样设置接口，/guest/**游客访问的接口  /user/**需要登入才能访问的接口   /admin/**管理员才能访问的接口
        //游客，开发权限
        map.put("/guest/**", "anon");
        //用户，需要角色权限 “user”
        map.put("/user/**", "roles[user]");
        //管理员，需要角色权限 “admin”
        map.put("/admin/**", "roles[admin]");
        //开放登陆接口
        map.put("/login", "anon");
        */

        //对所有用户认证
        map.put("/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        //登录
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //错误页面，认证不通过跳转  TODO   还没有实现
        shiroFilterFactoryBean.setUnauthorizedUrl("/index.html");

        return shiroFilterFactoryBean;
    }


	@Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(1000*60*4);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        return sessionManager;
    }


	  //没有实现  将自己的验证方式加入容器  //FIXME 需要将配置的credentialsMatcher设置到realm中
	  @Bean
	  public UserRealm myShiroRealm() {
	  	UserRealm myShiroRealm = new UserRealm();
	  	//将自定义的令牌set到了Realm
	  	myShiroRealm.setCredentialsMatcher(credentialsMatcher());
	  	//将cache加入realm中
	  	myShiroRealm.setCacheManager(ehCacheManager());
	     return myShiroRealm;
	  }

    @Bean(name = "securityManager")  //FIXME  将realm 设置到SecurityManager中
    								//FIXME  将sessionManager 设置到SecurityManager中
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());

        //FIXME  将CacheManager 设置到SecurityManager中         将RememberMeManager 设置到SecurityManager中

        //注入缓存管理器;   FIXME 使用缓存后，当用户修改了权限等缓存数据，不退出登入就会更新权限的缓存数据
        //注意:开发时请先关闭，如不关闭热启动会报错
        securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;

        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }


    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
	  * aop代理
	  * @return
	  */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心；
     *
     * @return
     */
    @Bean("ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }


    /**
     * cookie对象;
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        ////rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        //cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

//    @Bean(name = "shiroDialect")
//    public ShiroDialect shiroDialect() {
//        return new ShiroDialect();
//    }

	  /**
	  * 功能增强  设置credentialsMatcher的加密参数
	  * @param
	  * @return
	  */
	 @Bean
	 public CredentialsMatcher credentialsMatcher() {
	 	 HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
	 	 //加密方式
	 	 credentialsMatcher.setHashAlgorithmName("MD5");
	     //加密迭代次数
	     credentialsMatcher.setHashIterations(3);
//	     credentialsMatcher.setHashSalted(true); 默认是加盐的
	     //true加密用的hex编码，false用的base64编码  默认是用hex编码的，当设置为false时，使用hex编码
	     //String passwordEn = new SimpleHash("MD5",password,random,3).toHex();
	     credentialsMatcher.setStoredCredentialsHexEncoded(true);
	     //重新尝试的次数（自己定义的）
	     return credentialsMatcher;
	 }

}