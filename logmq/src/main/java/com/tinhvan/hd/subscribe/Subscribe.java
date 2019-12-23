package com.tinhvan.hd.subscribe;


import com.tinhvan.hd.config.RabbitConfig;

import com.tinhvan.hd.entity.CustomerLogAction;
import com.tinhvan.hd.entity.LogCallProcedureMiddleDB;
import com.tinhvan.hd.entity.LogEntity;
import com.tinhvan.hd.entity.StaffLogAction;
import com.tinhvan.hd.repository.CustomerLogActionRepository;
import com.tinhvan.hd.repository.LogCallProcedureRepository;
import com.tinhvan.hd.repository.LogRepository;
import com.tinhvan.hd.repository.StaffLogActionRepository;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Subscribe {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogCallProcedureRepository callProcedureRepository;

    @Autowired
    private StaffLogActionRepository staffLogActionRepository;

    @Autowired
    private CustomerLogActionRepository customerLogActionRepository;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receivedMessage(LogEntity msg) {
        try {
            msg.setCreateDate(new Date());
            logRepository.save(msg);
        }catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_LOG_PROCEDURE)
    public void insertProcedureLog(LogCallProcedureMiddleDB logCallProcedureMiddleDB) {
        try {
            logCallProcedureMiddleDB.setCreatedAt(new Date());
            callProcedureRepository.save(logCallProcedureMiddleDB);

        }catch (Exception e){
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    //Staff log action
    @RabbitListener(queues = RabbitConfig.QUEUE_LOG_STAFF_LOG_ACTION)
    public void insertStaffLogAction(StaffLogAction object) {
        try{
            object.setCreatedAt(new Date());
            staffLogActionRepository.save(object);
        }catch (Exception e){
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }

    }

    //Customer log action
    @RabbitListener(queues = RabbitConfig.QUEUE_LOG_CUSTOMER_LOG_ACTION)
    public void insertCustomerLogAction(CustomerLogAction object) {
        try {
            object.setCreatedAt(new Date());
            customerLogActionRepository.save(object);
        }catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }
}
