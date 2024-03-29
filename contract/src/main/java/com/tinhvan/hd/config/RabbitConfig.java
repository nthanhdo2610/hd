package com.tinhvan.hd.config;

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
public class RabbitConfig implements RabbitListenerConfigurer
{
    public static final String QUEUE_UPDATE_CONTRACT_FROM_MIDDLE = "queue_update_contract_middle_db";

    public static final String EXCHANGE_UPDATE_CONTRACT_FROM_MIDDLE = "exchange-update_contract_middle_db";

    public static final String QUEUE_LOG_PROCEDURE = "queue-log-procedure";

    public static final String EXCHANGE_LOG_PROCEDURE = "exchange-log-procedure";


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

    @Bean
    Queue queueLogProcedure() {
        return new Queue(QUEUE_LOG_PROCEDURE, false);
    }

    @Bean
    TopicExchange exchangeLogProcedure() {
        return new TopicExchange(EXCHANGE_LOG_PROCEDURE);
    }

    @Bean
    Binding bindingLogProcedure(Queue queueLogProcedure, TopicExchange exchangeLogProcedure) {
        return BindingBuilder.bind(queueLogProcedure).to(exchangeLogProcedure).with(QUEUE_LOG_PROCEDURE);
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

    //basic
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