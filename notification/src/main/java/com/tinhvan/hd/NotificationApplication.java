package com.tinhvan.hd;

import com.tinhvan.hd.base.HDContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableCaching
@EnableScheduling
public class NotificationApplication extends HDContext {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

}
