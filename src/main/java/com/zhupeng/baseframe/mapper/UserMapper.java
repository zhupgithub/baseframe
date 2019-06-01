package com.zhupeng.baseframe.mapper;

import com.zhupeng.baseframe.entity.db.User;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User> selectSelective(User user);
}