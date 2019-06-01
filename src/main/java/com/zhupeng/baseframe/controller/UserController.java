package com.zhupeng.baseframe.controller;

import com.google.code.kaptcha.Constants;
import com.zhupeng.baseframe.annotation.DebuggingLogAnnotation;
import com.zhupeng.baseframe.entity.co.user.LoginUserCo;
import com.zhupeng.baseframe.entity.co.user.UserRegisterCo;
import com.zhupeng.baseframe.entity.db.User;
import com.zhupeng.baseframe.entity.ro.ResponseResult;
import com.zhupeng.baseframe.service.UserService;
import com.zhupeng.baseframe.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Api(value = "/UserController" , tags = "用户相关接口" , description = "用户相关接口")
@RestController("userController")
@RequestMapping("userController")
public class UserController {
	@Autowired
	UserService userService;


	@DebuggingLogAnnotation
	@ApiOperation(value = "添加用户",notes = "添加用户" , httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE , consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)  
	@ApiResponses(value = { @ApiResponse(code = 1, message = "操作成功"),@ApiResponse(code = 2, message = "服务器内部异常"),@ApiResponse(code = 3, message = "权限不足")})  
	@RequestMapping(value = "/test",method = {RequestMethod.GET})
	public String addUser(){
		log.info("添加用户");
		User user = new User();
		user.setUsername("hh");
		user.setPassword("23");
		user.setSalt("22");
		userService.addUser(user);
		return "添加用户";
	}

	//实现 FormAuthenticationFilter 然后 在filter中进行验证码校验,或者是 在shiroRealm中进行验证码校验
	@RequestMapping("/loginUser")
	public ResponseResult loginUser(LoginUserCo loginUserCo , HttpSession session, HttpServletRequest request) {
//    	String username, char[] password, boolean rememberMe, String host, String captcha

		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken (loginUserCo.getUsername(), loginUserCo.getPassword(), loginUserCo.isRememberMe(), request.getRemoteHost());
//    	CaptchaUsernamePasswordToken usernamePasswordToken = new CaptchaUsernamePasswordToken(username,password,rememberMe,request.getRemoteHost(),captcha); //用户名和密码封装到UsernamePasswordToken中
//    	usernamePasswordToken.setRememberMe(rememberMe);
		if(loginUserCo.getCaptcha() == null || !loginUserCo.getCaptcha().equalsIgnoreCase(ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY))){
			throw new RuntimeException("验证码异常");
		}
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(usernamePasswordToken);   //完成登录
			if(subject.isAuthenticated()){
				User user = (User) subject.getPrincipal();
				session.setAttribute("user", user);
				return ResponseResult.success();
			}else{
				return ResponseResult.error();
			}

		}catch (UnknownAccountException ue){
			throw new RuntimeException("账号不存在");
		} catch (IncorrectCredentialsException ie){
			throw new RuntimeException("用户名/密码错误");
		}
	}
	@RequestMapping("/logOut")
	public String logOut(HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		session.removeAttribute("user");
		return "login";
	}
	/**
	 * 获得图形验证码
	 * @param response
	 */
	@RequestMapping("/Captcha.jpg")
	public void kaptcha(HttpServletResponse response){
		userService.kaptcha(response);
	}

	@DebuggingLogAnnotation
	@RequestMapping("/register")
	public ResponseResult register(@Valid UserRegisterCo userRegisterCo){
		return userService.register(userRegisterCo);
	}

	@RequiresRoles("admin")
	@RequestMapping("/getRole")
	public void getRole(){
		log.info("++++++++++++++++++++++管理员角色++++++++++++++++++++++++++++");
		//roleService.selectRolesByUid(user.getUid() == null ? 1 : user.getUid());
	}

	@RequiresPermissions("user:delete")
	@RequestMapping("/delete")
	public void delete(){
		log.info("++++++++++++++++++++++用户删除权限++++++++++++++++++++++++++++");
		//roleService.selectRolesByUid(user.getUid() == null ? 1 : user.getUid());
	}
}
