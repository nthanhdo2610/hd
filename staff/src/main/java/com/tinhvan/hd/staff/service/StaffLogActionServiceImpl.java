package com.tinhvan.hd.staff.service;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.staff.config.RabbitConfig;
import com.tinhvan.hd.staff.dao.StaffLogActionDAO;
import com.tinhvan.hd.staff.model.StaffLogAction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class StaffLogActionServiceImpl implements StaffLogActionService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    StaffLogActionDAO staffLogActionDAO;


    @Autowired
    public StaffLogActionServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createMQ(StaffLogAction object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_STAFF_LOG_ACTION, object);
    }

    @Override
    public List<StaffLogAction> list() {
        return staffLogActionDAO.list();
    }
}
