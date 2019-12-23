package com.tinhvan.hd.customer.rabbitmq;

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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

    //sms queue
    public static final String QUEUE_SEND_SMS = "queue-send-sms-queue";

    public static final String EXCHANGE_SEND_SMS = "exchange-send-sms-queue";

    @Bean
    Queue smsQueue() {
        return new Queue(QUEUE_SEND_SMS, false);
    }

    @Bean
    TopicExchange exchangeSMS() {
        return new TopicExchange(EXCHANGE_SEND_SMS);
    }

    @Bean
    Binding bindingSMS(Queue smsQueue, TopicExchange exchangeSMS) {
        return BindingBuilder.bind(smsQueue).to(exchangeSMS).with(QUEUE_SEND_SMS);
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

    //customer log action
    public static final String QUEUE_LOG_CUSTOMER_LOG_ACTION = "queue-log-customer-log-action";

    public static final String EXCHANGE_LOG_CUSTOMER_LOG_ACTION = "exchange-log-customer-log-action";

    @Bean
    Queue queueLogCustomerLogAction() {
//        Map<String, Object>  args = new HashMap<>();
//        args.put("x-dead-letter-exchange", "");
//        args.put("x-dead-letter-routing-key", "");
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

    //staff log action
    public static final String QUEUE_LOG_STAFF_LOG_ACTION = "queue-log-staff-log-action";

    public static final String EXCHANGE_LOG_STAFF_LOG_ACTION = "exchange-log-staff-log-action";

    @Bean
    Queue queueLogStaffLogAction() {
        return new Queue(QUEUE_LOG_STAFF_LOG_ACTION, false);
    }

    @Bean
    TopicExchange exchangeLogStaffLogAction() {
        return new TopicExchange(EXCHANGE_LOG_STAFF_LOG_ACTION);
    }

    @Bean
    Binding bindingLogStaffLogAction(Queue queueLogStaffLogAction, TopicExchange exchangeLogStaffLogAction) {
        return BindingBuilder.bind(queueLogStaffLogAction).to(exchangeLogStaffLogAction).with(QUEUE_LOG_STAFF_LOG_ACTION);
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
