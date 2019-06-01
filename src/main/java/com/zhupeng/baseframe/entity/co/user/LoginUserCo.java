package com.zhupeng.baseframe.entity.co.user;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginUserCo {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private boolean rememberMe;

    @NotBlank(message = "图形验证码不能为空")
    private String captcha;

}
