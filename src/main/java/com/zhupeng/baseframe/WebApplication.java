package com.zhupeng.baseframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class WebApplication {

	public static void main(String[] args) {
		log.info("启动开始……");
		SpringApplication.run(WebApplication.class, args);
		log.info("启动完成！");
	}
}
