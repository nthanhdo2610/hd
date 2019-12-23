package com.tinhvan.hd;

import com.tinhvan.hd.base.HDContext;
import com.tinhvan.hd.promotion.file.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableCaching
public class PromotionApplication extends HDContext {

    public static void main(String[] args) {
        SpringApplication.run(PromotionApplication.class, args);
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {

        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize("200MB");

        factory.setMaxRequestSize("200MB");
        return factory.createMultipartConfig();

    }

}



