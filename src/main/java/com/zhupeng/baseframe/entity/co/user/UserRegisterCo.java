package com.zhupeng.baseframe.entity.co.user;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserRegisterCo {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
