package com.tinhvan.hd;

import com.tinhvan.hd.filehandler.config.HDContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilehandlerApplication extends HDContext {

    public static void main(String[] args) {
        SpringApplication.run(FilehandlerApplication.class, args);
    }

}
