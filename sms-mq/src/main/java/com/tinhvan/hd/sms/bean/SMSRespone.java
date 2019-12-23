/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.bean;

/**
 *
 * @author LUUBI
 */
public class SMSRespone{

    private String phoneNumber;
    private String content;

    public SMSRespone() {      
    }
    
    public SMSRespone(String phoneNumber, String content) {
        this.phoneNumber = phoneNumber;
        this.content = content;

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SMSRespone{" + "phoneNumber=" + phoneNumber + ", content=" + content + '}';
    }
    
    

}
