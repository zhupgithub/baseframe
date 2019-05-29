package com.zhupeng.baseframe.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

public class CookieUtils {
    public static final String COOKIE_AUTHKEY = "accountmanage-authkey";

    /**
     * 获取cookie
     *
     * @return
     */
    public static Cookie getCookie(String name) {
        Cookie[] cookies = null;
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            cookies = attrs.getRequest().getCookies();
        } catch (Exception e) {
        }
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    /**
     * 保存cookie
     *
     * @return
     */
    public static void saveCookie(String cookieName, String cookieValue) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie cookie = getCookie(cookieName);
        if (cookie != null) {
            // 修改cookie时间戳
            cookie.setValue(cookieValue);
        } else {
            // 重新new一个Cookie
            cookie = new Cookie(cookieName, cookieValue);
        }
        cookie.setPath("/");// 同一个域名所有url cookie共享
        // cookie.setMaxAge(5*60);不写入磁盘，只写入内存，只有在当前页面有用,浏览器关闭立即失效
        attrs.getResponse().addCookie(cookie);
    }

    /**
     * 失效Cookie
     *
     */
    public static void removeCookie(String cookieName) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 失效cookie
        Cookie cookie = getCookie(cookieName);
        if (cookie != null) {
            cookie.setMaxAge(0);// 设置为0立即删除
            cookie.setPath("/");
            attrs.getResponse().addCookie(cookie);
        }
    }
}
