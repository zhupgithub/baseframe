package com.zhupeng.baseframe.service.impl;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.zhupeng.baseframe.common.CodeType;
import com.zhupeng.baseframe.entity.co.user.UserRegisterCo;
import com.zhupeng.baseframe.entity.db.User;
import com.zhupeng.baseframe.entity.ro.ResponseResult;
import com.zhupeng.baseframe.mapper.UserMapper;
import com.zhupeng.baseframe.service.UserService;
import com.zhupeng.baseframe.shiro.ShiroUtils;
import com.zhupeng.baseframe.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private Producer producer;

    @Override
    public void test() {
        log.info("测试");
        User sysUser = new User();

        sysUser.setUsername("zhupeng22");
        sysUser.setPassword("123");
        userMapper.insert(sysUser);
    }
    /**
     * 根据用户属性条件查询用户集合
     * @param user
     * @return
     */
    @Override
    public List<User> selectSelective(User user) {
        return userMapper.selectSelective(user);
    }

    @Override
    public void kaptcha(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
        } catch (IOException e) {
            log.error("二维码生成失败",e);
        }
    }

    @Override
    public ResponseResult register(UserRegisterCo userRegisterCo) {

        User userTemp = new User();
        userTemp.setUsername(userRegisterCo.getUsername());
        List<User> userList = userMapper.selectSelective(userTemp);
        if(userList != null && userList.size() >0){
            throw new RuntimeException("该用户名已经存在");
        }
        String random = UUIDUtils.getUUID();
        //使用md5和hex加密
        String passwordEn = new SimpleHash("MD5",userRegisterCo.getPassword(),random,3).toHex();

        User user = new User();
        user.setPassword(passwordEn);
        user.setUsername(userRegisterCo.getUsername());
        user.setSalt(random);
        userMapper.insertSelective(user);

        return ResponseResult.success(CodeType.USER_REGISTRATION_SUCCESS.getCode(),CodeType.USER_REGISTRATION_SUCCESS.getMsg(),user);
    }

    @Override
    public void addUser(User user) {
        userMapper.insertSelective(user);
    }

}
