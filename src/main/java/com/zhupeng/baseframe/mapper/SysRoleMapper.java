package com.zhupeng.baseframe.mapper;


import com.zhupeng.baseframe.entity.SysRole;

import java.util.List;

/**
 * 角色管理
 * 
 * @author zhupeng
 */
public interface SysRoleMapper {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);

	int deleteBatch(Long[] userId);

	int update(SysRole sysRole);

	int save(SysRole sysRole);

	SysRole queryObject(Long value);
}
