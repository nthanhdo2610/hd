/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.sms.invoke.ContractEsignedRequest;
import com.tinhvan.hd.sms.model.SMS;
import java.util.List;

/**
 *
 * @author LUUBI
 */
public interface SMSService {

    void create(SMS object);

    void update(SMS object);

    void mqSendSMS(SMS object);

    void mqVerifyOTPTypeEsign(ContractEsignedRequest contractEsignedRequest);
}
