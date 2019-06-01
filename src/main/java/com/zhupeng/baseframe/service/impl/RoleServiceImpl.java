package com.zhupeng.baseframe.service.impl;

import com.zhupeng.baseframe.entity.db.Role;
import com.zhupeng.baseframe.mapper.RoleMapper;
import com.zhupeng.baseframe.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    /**
     * 查询该用户的所有角色
     * @param uId
     * @return
     */
    @Override
    public List<Role> selectRolesByUid(Integer uId) {
        return roleMapper.selectRolesByUid(uId);
    }
}
