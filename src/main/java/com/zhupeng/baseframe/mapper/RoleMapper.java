package com.zhupeng.baseframe.mapper;


import com.zhupeng.baseframe.entity.db.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectRolesByUid(@Param("uid") Integer uid);
}