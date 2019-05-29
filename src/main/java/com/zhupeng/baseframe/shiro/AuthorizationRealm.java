package com.zhupeng.baseframe.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 统一角色授权控制策略
 */
@Slf4j
public class AuthorizationRealm extends AuthorizingRealm {

//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private SysMenuService sysMenuService;

    /**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
//		Long userId = user.getUserId();
//
//		//用户权限列表
//		Set<String> permsSet = sysMenuService.getUserPermissions(userId);
//
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.setStringPermissions(permsSet);
//		return info;
        return null;
	}

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        return null;
    }

}
