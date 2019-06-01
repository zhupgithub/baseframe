package com.zhupeng.baseframe.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
    	
    	
//    	(Constants.KAPTCHA_SESSION_CONFIG_KEY, Constants.KAPTCHA_SESSION_KEY);//session key
//      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "50");//字体大小
//      servlet.addInitParameter(Constants.KAPTCHA_BORDER, "no");  //是否有边框 可选yes 或者 no
//      servlet.addInitParameter(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");  //边框颜色
//      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "45");  ///验证码文本字符大小
//      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");  //验证码文本字符长度  默认为5
//      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");//验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize) 
//      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");  // 验证码文本字符颜色
//      servlet.addInitParameter(Constants.KAPTCHA_IMAGE_WIDTH, "125");  //验证码图片的宽度 默认200
//      servlet.addInitParameter(Constants.KAPTCHA_IMAGE_HEIGHT, "60");  // 验证码图片的高度 默认50
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
    
	//配置kaptcha图片验证码框架提供的Servlet,,这是个坑,很多人忘记注册(注意)
  @Bean
  public ServletRegistrationBean kaptchaServlet() {
      ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha.jpg");
      servlet.addInitParameter(Constants.KAPTCHA_SESSION_CONFIG_KEY, Constants.KAPTCHA_SESSION_KEY);//session key
      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "50");//字体大小
      servlet.addInitParameter(Constants.KAPTCHA_BORDER, "no");  //是否有边框 可选yes 或者 no
      servlet.addInitParameter(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");  //边框颜色
      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "45");  ///验证码文本字符大小
      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");  //验证码文本字符长度  默认为5
      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");//验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
      servlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");  // 验证码文本字符颜色
      servlet.addInitParameter(Constants.KAPTCHA_IMAGE_WIDTH, "125");  //验证码图片的宽度 默认200
      servlet.addInitParameter(Constants.KAPTCHA_IMAGE_HEIGHT, "60");  // 验证码图片的高度 默认50
      //可以设置很多属性,具体看com.google.code.kaptcha.Constants
      return servlet;
  }
}