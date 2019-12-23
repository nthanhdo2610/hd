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
    //notification
    public static final String QUEUE_SEND_NOTIFICATIONS = "queue-send-notifications";

    public static final String EXCHANGE_SEND_NOTIFICATIONS = "exchange-send-notifications";

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

    //unsubscribe topic firebase
    public static final String QUEUE_UNSUBSCRIBE_TOPIC_FIREBASE = "queue-unsubscribe-topic-firebase";
    public static final String EXCHANGE_UNSUBSCRIBE_TOPIC_FIREBASE = "exchange-unsubscribe-topic-firebase";

    @Bean
    Queue unsubscribeQueue() {
        return new Queue(QUEUE_UNSUBSCRIBE_TOPIC_FIREBASE, false);
    }

    @Bean
    TopicExchange exchangeUnsubscribe() {
        return new TopicExchange(EXCHANGE_UNSUBSCRIBE_TOPIC_FIREBASE);
    }

    @Bean
    Binding bindingUnsubscribe(Queue unsubscribeQueue, TopicExchange exchangeUnsubscribe) {
        return BindingBuilder.bind(unsubscribeQueue).to(exchangeUnsubscribe).with(QUEUE_UNSUBSCRIBE_TOPIC_FIREBASE);
    }

    //subscribe topic firebase
    public static final String QUEUE_SUBSCRIBE_TOPIC_FIREBASE = "queue-subscribe-topic-firebase";
    public static final String EXCHANGE_SUBSCRIBE_TOPIC_FIREBASE = "exchange-subscribe-topic-firebase";

    @Bean
    Queue subscribeQueue() {
        return new Queue(QUEUE_SUBSCRIBE_TOPIC_FIREBASE, false);
    }

    @Bean
    TopicExchange exchangeSubscribe() {
        return new TopicExchange(EXCHANGE_SUBSCRIBE_TOPIC_FIREBASE);
    }

    @Bean
    Binding bindingSubscribe(Queue subscribeQueue, TopicExchange exchangeSubscribe) {
        return BindingBuilder.bind(subscribeQueue).to(exchangeSubscribe).with(QUEUE_SUBSCRIBE_TOPIC_FIREBASE);
    }

    //sms queue
    public static final String QUEUE_SEND_SMS_QUEUE = "queue-send-sms-queue";

    public static final String EXCHANGE_SEND_SMS_QUEUE = "exchange-send-sms-queue";

    @Bean
    Queue smsQueue() {
        return new Queue(QUEUE_SEND_SMS_QUEUE, false);
    }

    @Bean
    TopicExchange exchangeSMSQueue() {
        return new TopicExchange(EXCHANGE_SEND_SMS_QUEUE);
    }

    @Bean
    Binding bindingSMSQueue(Queue smsQueue, TopicExchange exchangeSMSQueue) {
        return BindingBuilder.bind(smsQueue).to(exchangeSMSQueue).with(QUEUE_SEND_SMS_QUEUE);
    }

    //mqVerifyOTPTypeEsign
    public static final String QUEUE_SEND_VERIFY_OTP_QUEUE = "queue-send-verify-otp-queue";

    public static final String EXCHANGE_SEND_VERIFY_OTP_QUEUE = "exchange-send-verify-otp-queue";

    @Bean
    Queue verifyOtpQueue() {
        return new Queue(QUEUE_SEND_VERIFY_OTP_QUEUE, false);
    }

    @Bean
    TopicExchange exchangeVerifyOtpQueue() {
        return new TopicExchange(EXCHANGE_SEND_VERIFY_OTP_QUEUE);
    }

    @Bean
    Binding bindingVerifyOtpQueue(Queue verifyOtpQueue, TopicExchange exchangeVerifyOtpQueue) {
        return BindingBuilder.bind(verifyOtpQueue).to(exchangeVerifyOtpQueue).with(QUEUE_SEND_VERIFY_OTP_QUEUE);
    }

    //email queue
    public static final String QUEUE_SEND_EMAIL_QUEUE = "queue-send-email-queue";

    public static final String EXCHANGE_SEND_EMAIL_QUEUE = "exchange-send-email-queue";

    @Bean
    Queue emailQueue() {
        return new Queue(QUEUE_SEND_EMAIL_QUEUE, false);
    }

    @Bean
    TopicExchange exchangeEmailQueue() {
        return new TopicExchange(EXCHANGE_SEND_EMAIL_QUEUE);
    }

    @Bean
    Binding bindingEmailQueue(Queue emailQueue, TopicExchange exchangeEmailQueue) {
        return BindingBuilder.bind(emailQueue).to(exchangeEmailQueue).with(QUEUE_SEND_EMAIL_QUEUE);
    }



    // contract queue
    public static final String QUEUE_UPDATE_CONTRACT_FROM_MIDDLE = "queue_update_contract_middle_db";

    public static final String EXCHANGE_UPDATE_CONTRACT_FROM_MIDDLE = "exchange-update_contract_middle_db";

    @Bean
    Queue contractQueue() {
        return new Queue(QUEUE_UPDATE_CONTRACT_FROM_MIDDLE, false);
    }

    @Bean
    TopicExchange exchangeContract() {
        return new TopicExchange(EXCHANGE_UPDATE_CONTRACT_FROM_MIDDLE);
    }

    @Bean
    Binding bindingContract(Queue contractQueue, TopicExchange exchangeContract) {
        return BindingBuilder.bind(contractQueue).to(exchangeContract).with(QUEUE_UPDATE_CONTRACT_FROM_MIDDLE);
    }


    //bean config
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
}