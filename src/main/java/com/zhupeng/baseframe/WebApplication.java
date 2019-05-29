package com.zhupeng.baseframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling //开启定时任务
@EnableAsync //开启异步调用
@MapperScan("com.zhupeng.baseframe.mapper")
public class WebApplication {

	public static void main(String[] args) {
		log.info("启动开始……");
		SpringApplication.run(WebApplication.class, args);
		log.info("启动完成！");
	}
}
