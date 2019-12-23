/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.service;

import com.tinhvan.hd.email.model.Email;
import com.tinhvan.hd.email.rabbitmq.EmailRequest;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public interface EmailService {

    void create(Email object);

    void update(Email object);

    Email existEmail(String email);

    Email findByUUID(UUID uuid);

    void sendEmail(EmailRequest emailRequest);
}
