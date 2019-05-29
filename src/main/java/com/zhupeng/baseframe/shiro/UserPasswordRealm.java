package com.zhupeng.baseframe.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 用户密码登录realm
 */
@Slf4j
public class UserPasswordRealm extends AuthorizingRealm {
//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private SysMenuService sysMenuService;


    @Override
    public String getName() {
        return LoginType.USER_PASSWORD.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PASSWORD;
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
//        log.info("---------------- 用户密码登录 ----------------------");
//        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        String name = token.getUsername();
//        String password = new String((char[]) token.getCredentials());
//        // 从数据库获取对应用户名密码的用户
//        SysUserEntity user = sysUserService.queryByUserName(name);
//        //账号不存在
//        if(user == null) {
//            throw new UnknownAccountException("账号或密码不正确");
//        }
//
//        //密码错误
//        if(!password.equals(user.getPassword())) {
//            throw new IncorrectCredentialsException("账号或密码不正确");
//        }
//
//        //账号锁定
//        if(user.getStatus() == 0){
//        	throw new LockedAccountException("账号已被锁定,请联系管理员");
//        }
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                user, //用户
//                user.getPassword(), //密码
//                getName()  //realm name
//        );
//        return authenticationInfo;
        return null;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
