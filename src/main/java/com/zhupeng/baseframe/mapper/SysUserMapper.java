package com.zhupeng.baseframe.mapper;

import com.zhupeng.baseframe.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author zhueng
 */
public interface SysUserMapper {

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUser queryByUserName(String username);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);

	/***
	 * 查询最大用户工号
	 */
	String queryMaxUserNumber();

//	List<SysRole> queryRoleOfUser(Map<String, Object> map);

    SysUser getUserByNameAndPassword(@Param("username") String username, @Param("password") String password);

    SysUser queryByMobileNo(@Param("mobileNo") String mobileNo);

    int deleteBatch(Long[] userId);

    int update(SysUser sysUser);

    int save(SysUser sysUser);

	SysUser queryObject(Long value);
}
