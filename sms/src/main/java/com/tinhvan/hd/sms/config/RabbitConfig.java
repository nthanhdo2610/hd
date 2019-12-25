package com.tinhvan.hd.sms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
public class RabbitConfig implements RabbitListenerConfigurer {

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

    //customer log action
    public static final String QUEUE_LOG_CUSTOMER_LOG_ACTION = "queue-log-customer-log-action";

    public static final String EXCHANGE_LOG_CUSTOMER_LOG_ACTION = "exchange-log-customer-log-action";

    @Bean
    Queue queueLogCustomerLogAction() {
        return new Queue(QUEUE_LOG_CUSTOMER_LOG_ACTION, false);
    }

    @Bean
    TopicExchange exchangeLogCustomerLogAction() {
        return new TopicExchange(EXCHANGE_LOG_CUSTOMER_LOG_ACTION);
    }

    @Bean
    Binding bindingLogCustomerLogAction(Queue queueLogCustomerLogAction, TopicExchange exchangeLogCustomerLogAction) {
        return BindingBuilder.bind(queueLogCustomerLogAction).to(exchangeLogCustomerLogAction).with(QUEUE_LOG_CUSTOMER_LOG_ACTION);
    }


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
