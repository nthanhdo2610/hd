package com.tinhvan.hd.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer
{
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    public static final String QUEUE_SEND_NOTIFICATIONS = "queue-send-notifications";

    public static final String EXCHANGE_SEND_NOTIFICATIONS = "exchange-send-notifications";

    //notification
    @Bean
    Queue notification() {
        return new Queue(QUEUE_SEND_NOTIFICATIONS, false);
    }

    @Bean
    TopicExchange exchangeNotification() {
        return new TopicExchange(EXCHANGE_SEND_NOTIFICATIONS);
    }

    @Bean
    Binding bindingNotification(Queue notification, TopicExchange exchangeNotification) {
        return BindingBuilder.bind(notification).to(exchangeNotification).with(QUEUE_SEND_NOTIFICATIONS);
    }

    //notification queue

    public static final String QUEUE_SEND_NOTIFICATION_QUEUE = "queue-send-notification-queue";

    public static final String EXCHANGE_SEND_NOTIFICATION_QUEUE = "exchange-send-notification-queue";

    @Bean
    Queue notificationQueue() {
        return new Queue(QUEUE_SEND_NOTIFICATION_QUEUE, false);
    }

    @Bean
    TopicExchange exchangeNotificationQueue() {
        return new TopicExchange(EXCHANGE_SEND_NOTIFICATION_QUEUE);
    }

    @Bean
    Binding bindingNotificationQueue(Queue notificationQueue, TopicExchange exchangeNotificationQueue) {
        return BindingBuilder.bind(notificationQueue).to(exchangeNotificationQueue).with(QUEUE_SEND_NOTIFICATION_QUEUE);
    }

    //update notification queue
    public static final String QUEUE_UPDATE_NOTIFICATION = "queue-update-notification";
    public static final String EXCHANGE_UPDATE_NOTIFICATION = "exchange-update-notification";

    @Bean
    Queue updateNotificationQueue() {
        return new Queue(QUEUE_UPDATE_NOTIFICATION, false);
    }

    @Bean
    TopicExchange exchangeUpdateNotification() {
        return new TopicExchange(EXCHANGE_UPDATE_NOTIFICATION);
    }

    @Bean
    Binding bindingUpdateNotification(Queue updateNotificationQueue, TopicExchange exchangeUpdateNotification) {
        return BindingBuilder.bind(updateNotificationQueue).to(exchangeUpdateNotification).with(QUEUE_UPDATE_NOTIFICATION);
    }
}