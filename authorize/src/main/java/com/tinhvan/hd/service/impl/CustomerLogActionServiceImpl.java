package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.service.CustomerLogActionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerLogActionServiceImpl implements CustomerLogActionService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerLogActionServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createMQ(CustomerLogAction object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_CUSTOMER_LOG_ACTION, object);
    }
}
