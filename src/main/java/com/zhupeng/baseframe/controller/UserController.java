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
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

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

	@DebuggingLogAnnotation
	@RequestMapping("/zhu/{deviceId2}/{personId}/test")
	public String test(@PathVariable("deviceId2") int deviceId , @PathVariable int personId , String name , HttpServletRequest request){
		System.out.println(request);
		request.getParameterValues("deviceId");
		request.getAttribute("pathVariables");
		request.getParameterMap().get("name");

		//获得restful接口中的参数，/zhu/{deviceId2}/{personId}/test
		Object deviceO = ((HashMap<String,Object>)request.getAttribute("org.springframework.web.servlet.View.pathVariables")).get("deviceId");
        Object personO = ((HashMap<String,Object>)request.getAttribute("org.springframework.web.servlet.View.pathVariables")).get("personId");

		int deviceIdT =  Integer.parseInt(deviceO.toString());
		int personIdT =  Integer.parseInt(personO.toString());
		return "deviceId =" + deviceId +", personId= " + personId + ",name =" + name + ", deviceIdT = " + deviceIdT + "personIdT =" + personIdT;
	}
	//org.springframework.web.servlet.View.pathVariables -> {HashMap@11178}  size = 2

	/**
	 * 添加人脸
	 */
	@DebuggingLogAnnotation
	@PostMapping("/device/{deviceId}/test")
	public void addFace(@PathVariable Long deviceId , @RequestParam("file") MultipartFile file , Long personId , HttpServletRequest request) {
		System.out.println("hh" + request);
	}


	public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
		Class<UserController> clazz = UserController.class;

		//判断类上是否有相应注解
		Boolean isRequestMappingExist = clazz.isAnnotationPresent(RequestMapping.class);
		if(isRequestMappingExist){
			//获取注解定义在类上的注解
			RequestMapping requestMapping =  clazz.getAnnotation(RequestMapping.class);
			//获取注解中的属性值
			System.out.println(requestMapping.value() + " " + requestMapping.produces());
		}

		Field userService = clazz.getDeclaredField("userService");
		userService.setAccessible(true);
		Boolean isAutowiredExist = userService.isAnnotationPresent(Autowired.class);
		if(isAutowiredExist){
			//获取定义在属性上的注解
			Autowired autowired	= userService.getAnnotation(Autowired.class);
			//获取注解中的属性值
			System.out.println(autowired.required());
		}


		Method [] methods = clazz.getMethods();
		//获取方法上的注解
		Method method = clazz.getDeclaredMethod("loginUser");
		Boolean isRequestMappingMethod = method.isAnnotationPresent(RequestMapping.class);

		if(isRequestMappingMethod){
			RequestMapping requestMappingMethod = method.getAnnotation(RequestMapping.class);
			//获取注解中的属性值
			System.out.println(requestMappingMethod.value() + " " + requestMappingMethod.produces());
		}


	}
}
