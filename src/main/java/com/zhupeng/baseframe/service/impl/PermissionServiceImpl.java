package com.zhupeng.baseframe.service.impl;

import com.zhupeng.baseframe.entity.db.Permission;
import com.zhupeng.baseframe.mapper.PermissionMapper;
import com.zhupeng.baseframe.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;
    /**
     * 获得该角色用户的所有权限
     * @param rid
     * @return
     */
    @Override
    public List<Permission> selectPermissionsByRid(Integer rid) {
        return permissionMapper.selectPermissionsByRid(rid);
    }
}
