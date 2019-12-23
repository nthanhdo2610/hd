package com.tinhvan.hd;

import com.tinhvan.hd.base.HDContext;
import com.tinhvan.hd.file.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableCaching
public class AuthorizeApplication extends HDContext {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeApplication.class, args);
    }

}
