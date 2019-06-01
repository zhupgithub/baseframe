package com.zhupeng.baseframe.service;

import com.zhupeng.baseframe.entity.co.user.UserRegisterCo;
import com.zhupeng.baseframe.entity.db.Role;
import com.zhupeng.baseframe.entity.db.User;
import com.zhupeng.baseframe.entity.ro.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    void test();

    /**
     * 根据用户属性条件查询用户集合
     * @param user
     * @return
     */
    List<User> selectSelective(User user);

    /**
     * 生成图形验证码
     * @param response
     */
    void kaptcha(HttpServletResponse response);

    /**
     * 用户注册
     * @param userRegisterCo
     */
    ResponseResult register(UserRegisterCo userRegisterCo);

    /**
     * 测试使用
     * @param user
     */
    void addUser(User user);
}
