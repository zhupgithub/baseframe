package com.zhupeng.baseframe.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 手机验证码登录realm
 */
@Slf4j
public class UserPhoneRealm extends AuthorizingRealm {

//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private SysMenuService sysMenuService;

    @Override
    public String getName() {
        return LoginType.USER_PHONE.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PHONE;
        } else {
            return false;
        }
    }


    @Override
    public void setAuthorizationCacheName(String authorizationCacheName) {
        super.setAuthorizationCacheName(authorizationCacheName);
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
//        log.info("---------------- 手机验证码登录 ----------------------");
//        UserToken token = (UserToken) authcToken;
//        String phone = token.getUsername();
//        // 手机验证码
//        String validCode = String.valueOf(token.getPassword());
//
//        SysUserEntity user = sysUserService.queryByMobileNo(phone);
//        //账号不存在
//        if(user == null) {
//            throw new UnknownAccountException("账号或密码不正确");
//        }
//        //账号锁定
//        if(user.getStatus() == 0){
//            throw new LockedAccountException("账号已被锁定,请联系管理员");
//        }
//
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                user, //用户
//                validCode, //密码
//                getName()  //realm name
//        );
//        return authenticationInfo;
        return null;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


}
