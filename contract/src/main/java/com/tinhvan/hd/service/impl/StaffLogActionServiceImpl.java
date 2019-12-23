package com.tinhvan.hd.service.impl;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.service.StaffLogActionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffLogActionServiceImpl implements StaffLogActionService {

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public StaffLogActionServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    public void createMQ(StaffLogAction staffLogAction) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_STAFF_LOG_ACTION, staffLogAction);
    }
}
