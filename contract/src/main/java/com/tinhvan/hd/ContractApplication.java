package com.tinhvan.hd;

import com.tinhvan.hd.base.HDContext;
import com.tinhvan.hd.file.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableCaching
public class ContractApplication extends HDContext {

    public static void main(String[] args) {
        SpringApplication.run(ContractApplication.class, args);
        System.out.println(new Date());
    }

}
