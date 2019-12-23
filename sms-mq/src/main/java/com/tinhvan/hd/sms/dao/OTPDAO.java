/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.sms.model.OTP;

/**
 *
 * @author LUUBI
 */
public interface OTPDAO {
    OTP createOrUpdate(OTP object);
   
}
