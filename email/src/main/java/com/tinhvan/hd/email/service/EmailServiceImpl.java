/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.service;

import com.tinhvan.hd.email.model.Email;
import java.util.UUID;
import com.tinhvan.hd.email.dao.EmailDAO;
import com.tinhvan.hd.email.rabbitmq.EmailRequest;
import com.tinhvan.hd.email.config.RabbitConfig;
import com.tinhvan.hd.email.repository.EmailRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author LUUBI
 */
@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class EmailServiceImpl implements EmailService {

    @Autowired
    EmailDAO mailDAO;

    @Autowired
    private EmailRepository emailRepository;
    
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EmailServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void create(Email object) {
        emailRepository.save(object);
    }

    @Override
    public void update(Email object) {
        emailRepository.save(object);
    }

    @Override
    public Email existEmail(String email) {
        return mailDAO.existEmail(email);
    }

    @Override
    public Email findByUUID(UUID uuid) {
        return mailDAO.findByUUID(uuid);
    }

    @Override
    public void sendEmail(EmailRequest object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_EMAIL_QUEUE, object);
    }

}
