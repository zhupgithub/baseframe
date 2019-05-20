package com.zhupeng.test.mybatisGenerator.dao;

import com.zhupeng.test.mybatisGenerator.entity.AppProclamation;

public interface AppProclamationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppProclamation record);

    int insertSelective(AppProclamation record);

    AppProclamation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppProclamation record);

    int updateByPrimaryKey(AppProclamation record);
}