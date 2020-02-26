/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.service;


import com.tinhvan.hd.entity.SMS;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public interface SMSService {

    SMS createOrUpdate(SMS object);

    SMS findByUUID(UUID uuid);

    List<SMS> getList(int size);

    List<SMS> getListUpdateSMSLogs();
}
