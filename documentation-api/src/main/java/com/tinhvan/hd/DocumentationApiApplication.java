package com.tinhvan.hd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author satish sharma
 *
 */
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class DocumentationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentationApiApplication.class, args);
	}
}
