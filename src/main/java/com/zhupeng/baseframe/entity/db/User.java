package com.zhupeng.baseframe.entity.db;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * user
 * @author 朱朋 
 * @date 2019-06-01
 */
@ApiModel(value = "用户")
public class User implements Serializable {

    /**
     *
     */
    @ApiModelProperty(value = "" , name = "uid" , example = "" , required = true , dataType = "Integer" ,notes = "")
    private Integer uid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称" , name = "username" , example = "" , required = false , dataType = "String" ,notes = "用户名称")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码" , name = "password" , example = "" , required = false , dataType = "String" ,notes = "用户密码")
    private String password;

    /**
     * 用户密码加密的盐
     */
    @ApiModelProperty(value = "用户密码加密的盐" , name = "salt" , example = "" , required = false , dataType = "String" ,notes = "用户密码加密的盐")
    private String salt;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}