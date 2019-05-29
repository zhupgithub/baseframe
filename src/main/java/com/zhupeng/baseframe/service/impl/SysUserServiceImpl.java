package com.zhupeng.baseframe.service.impl;

import com.zhupeng.baseframe.entity.SysUser;
import com.zhupeng.baseframe.mapper.SysUserMapper;
import com.zhupeng.baseframe.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public void test() {
        log.info("测试");
        SysUser sysUser = new SysUser();

        sysUser.setUsername("zhupeng22");
        sysUser.setPassword("123");
        sysUser.setMerchantCode("zhupeng");
        sysUserMapper.save(sysUser);
    }
}
