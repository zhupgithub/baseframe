package com.zhupeng.baseframe.service;

import com.zhupeng.baseframe.entity.db.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 获得该角色用户的所有权限
     * @param rid
     * @return
     */
    List<Permission> selectPermissionsByRid(Integer rid);
}
