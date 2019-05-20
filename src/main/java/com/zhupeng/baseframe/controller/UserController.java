package com.zhupeng.baseframe.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "/UserController" , tags = "用户相关接口" , description = "用户相关接口")
@RestController("userController")
@RequestMapping("userController")
public class UserController {
	
	
	@ApiOperation(value = "添加用户",notes = "添加用户" , httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE , consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)  
	@ApiResponses(value = { @ApiResponse(code = 1, message = "操作成功"),@ApiResponse(code = 2, message = "服务器内部异常"),@ApiResponse(code = 3, message = "权限不足")})  
	@RequestMapping(value = "/add",method = {RequestMethod.GET})
	public String addUser(){
		log.info("添加用户");
		return "添加用户";
	}
	
	

}
