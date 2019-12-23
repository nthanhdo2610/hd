package com.tinhvan.hd;

import com.tinhvan.hd.base.HDContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HDSaisonEmailApplication extends HDContext{

    public static void main(String[] args) {
        SpringApplication.run(HDSaisonEmailApplication.class, args);
    }

}
