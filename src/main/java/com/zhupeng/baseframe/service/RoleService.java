package com.zhupeng.baseframe.service;

import com.zhupeng.baseframe.entity.db.Role;

import java.util.List;

public interface RoleService {
    /**
     * 查询该用户的所有角色
     * @param uId
     * @return
     */
    List<Role> selectRolesByUid(Integer uId);
}
