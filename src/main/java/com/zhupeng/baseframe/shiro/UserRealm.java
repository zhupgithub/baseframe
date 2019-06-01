package com.zhupeng.baseframe.shiro;

import com.zhupeng.baseframe.entity.db.Permission;
import com.zhupeng.baseframe.entity.db.Role;
import com.zhupeng.baseframe.entity.db.User;
import com.zhupeng.baseframe.service.PermissionService;
import com.zhupeng.baseframe.service.RoleService;
import com.zhupeng.baseframe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class UserRealm extends AuthorizingRealm {


	@Autowired
//	@Lazy  //注意此处需要添加@Lazy注解，否则SysUserService缓存注解、事务注解不生效
	UserService userService;

	@Autowired
//	@Lazy
	RoleService roleService;

	@Autowired
//	@Lazy
	PermissionService permissionService;

    /**
     * 此方法调用hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
         * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
         * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
         * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
         * 缓存过期之后会再次执行。
         */
		Set<String> roleSet = new HashSet<>();
		Set<String>  permissionSet = new HashSet<>();
		User user  = (User) principals.getPrimaryPrincipal();
		Integer uId = user.getUid();
		List<Role> roleList =  roleService.selectRolesByUid(uId);
		if(roleList != null && roleList.size() > 0){
			for(Role role : roleList){
				roleSet.add(role.getRname());
				List<Permission> permissionList = permissionService.selectPermissionsByRid(role.getRid());
				if(permissionList != null && permissionList.size() > 0){
					for(Permission permission : permissionList){
						permissionSet.add(permission.getPname());
					}
				}
			}
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roleSet);
		authorizationInfo.setStringPermissions(permissionSet);
		return authorizationInfo;
	}

	/**
	 * shiro 登入  认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//通过username从数据库中查找 ManagerInfo对象
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法

		String username = (String) token.getPrincipal();
		String password =  new String((char[]) token.getCredentials());

		log.info("username:"+username+"password:"+password);
		User userTemp = new User();


		userTemp.setUsername(username);

		List<User> userList = userService.selectSelective(userTemp);

		if(userList == null || userList.size() <= 0) {
			throw new RuntimeException("不存在该用户");
		}

		User user  = userList.get(0);
		String salt  = user.getSalt();

		ByteSource saltSource = ByteSource.Util.bytes(salt);

		//这里的用户的密码验证时有shiro来执行的，前端传入的明文（用户名、密码都是明文）存入到toke 中， shiro中将与这里传入的密文存入到SimpleAuthenticationInfo  ，来进行比对
		//在shiro的配置类中，设置了将明文转成密文的方式，用户只需要在添加用户时来进行加密，验证时，都是由shiro来进行验证的
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),saltSource,this.getName());

		//SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,password,this.getName());

		return authenticationInfo;
	}


//	   /**  FIXME  不一定需要
//     * 设置认证加密方式
//     */
//    @Override
//    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//        HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
//        md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.HASH_ALGORITHM_NAME);
//        md5CredentialsMatcher.setHashIterations(ShiroKit.HASH_ITERATIONS);
//        super.setCredentialsMatcher(md5CredentialsMatcher);
//    }
}
