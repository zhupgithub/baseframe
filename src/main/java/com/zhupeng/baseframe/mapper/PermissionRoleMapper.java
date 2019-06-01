package com.zhupeng.baseframe.mapper;


import com.zhupeng.baseframe.entity.db.PermissionRole;

public interface PermissionRoleMapper {
    int insert(PermissionRole record);

    int insertSelective(PermissionRole record);
}