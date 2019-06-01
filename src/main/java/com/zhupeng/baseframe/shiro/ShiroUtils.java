package com.zhupeng.baseframe.shiro;

import com.zhupeng.baseframe.entity.db.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * 
 * @author zhupeng

 * @date  2018年12月17日

 * @Description:shiro工具类
 */
public class ShiroUtils {

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static User getUserEntity() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	public static int getUserId() {
		return getUserEntity().getUid();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	public static String getKaptcha(String key) {
		String kaptcha = null;
		if(getSessionAttribute(key) != null) {
			kaptcha = getSessionAttribute(key).toString();
			getSession().removeAttribute(key);
		}
		return kaptcha;
	}
	
	/** 
     * 重新赋值权限(在比如:给一个角色临时添加一个权限,需要调用此方法刷新权限,否则还是没有刚赋值的权限) 
     * @param userRealm 自定义的realm
     * @param username 用户名 
     */  
    public static void reloadAuthorizing(UserRealm userRealm,String username){  
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();   
        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户   
        SimplePrincipalCollection principals = new SimplePrincipalCollection(username,realmName);
        subject.runAs(principals);   
        userRealm.getAuthorizationCache().remove(subject.getPrincipals());   
        subject.releaseRunAs();  
    }
}
