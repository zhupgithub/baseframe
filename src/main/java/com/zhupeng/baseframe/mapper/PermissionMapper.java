package com.zhupeng.baseframe.mapper;


import com.zhupeng.baseframe.entity.db.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectPermissionsByRid(@Param("rid") Integer rid);
}