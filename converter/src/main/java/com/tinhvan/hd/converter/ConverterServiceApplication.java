package com.tinhvan.hd.converter;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAsync
public class ConverterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConverterServiceApplication.class, args);
    }

    /*@Bean("ExecutorBean")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Converter-");
        executor.initialize();
        return executor;
    }*/
    @Bean
    public IConverter getConverter(){
        return LocalConverter.builder()
                .workerPool(4, 4, 5, TimeUnit.SECONDS)
                //.requestTimeout(10, TimeUnit.SECONDS)
                //.baseUri("http://192.168.75.104:8811")
                .build();
    }

}
