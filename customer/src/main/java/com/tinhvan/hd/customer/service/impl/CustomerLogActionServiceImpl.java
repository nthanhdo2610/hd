package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.customer.dao.CustomerLogActionDAO;
import com.tinhvan.hd.customer.payload.CustomerLogActionResponse;
import com.tinhvan.hd.customer.payload.CustomerLogActionSearch;
import com.tinhvan.hd.customer.rabbitmq.RabbitConfig;
import com.tinhvan.hd.customer.repository.CustomerLogActionRepository;
import com.tinhvan.hd.customer.service.CustomerLogActionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerLogActionServiceImpl implements CustomerLogActionService {

    @Autowired
    CustomerLogActionDAO customerLogActionDAO;

    @Autowired
    private CustomerLogActionRepository customerLogActionRepository;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerLogActionServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void create(CustomerLogAction customerLogAction) {
        customerLogActionRepository.save(customerLogAction);
    }

    @Override
    public void createMQ(CustomerLogAction object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_CUSTOMER_LOG_ACTION, object);
    }

    @Override
    public List<CustomerLogActionResponse> search(CustomerLogActionSearch object) {
        return customerLogActionDAO.search(object);
    }
}
