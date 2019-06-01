package com.zhupeng.baseframe.mapper;


import com.zhupeng.baseframe.entity.db.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}