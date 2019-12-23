package com.tinhvan.hd.base;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

public class HDContext {

	@Autowired
	private CacheService cacheService;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
	}

	@PreDestroy
 	public void onShutDown() {
		//Log.system("[onShutdown]");
		//MQ.disposeAll();
		cacheService.forgetAboutThis(HDConstant.SYSTEM_CONFIG);
		//HDRedis.disposeAll();
		//HDEntityManager.disposeAll();
	 }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//						.allowedOrigins("*")
//						.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
//						.maxAge(3600);
//			}
			public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
		};
	}

}
