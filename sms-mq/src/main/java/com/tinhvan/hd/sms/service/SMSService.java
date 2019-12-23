/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.sms.model.SMS;

/**
 *
 * @author LUUBI
 */
public interface SMSService {

    SMS createOrUpdate(SMS object);

    //    void callOTPMQ(OTPMQ object);
}
