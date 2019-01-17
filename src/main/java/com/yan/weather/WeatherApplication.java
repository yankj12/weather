package com.yan.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enable Scheduling ensures that a background task executor
					// is created. Without it, nothing gets scheduled.
public class WeatherApplication {

	// 方便未受spring管理的普通类获取上下文中的Service
	private static ApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(WeatherApplication.class, args);
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}
}
