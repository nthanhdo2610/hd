package com.tinhvan.hd.base;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("rabbitLog")
public class RabbitConfig {

    public static final String QUEUE = "logQueue";

    public static final String EXCHANGE = "logExchange";

    @Bean
    Queue logQueue() {
        return new Queue(QUEUE, false);
    }


    @Bean
    TopicExchange logExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding logBinding(Queue logQueue, TopicExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with(QUEUE);
    }
}
