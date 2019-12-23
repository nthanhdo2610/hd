package com.tinhvan.hd.customer.rabbitmq;

import com.tinhvan.hd.base.HDPayload;

public class SMSResponse implements HDPayload {

    private String phoneNumber;
    private String content;

    public SMSResponse(String phoneNumber, String content) {
        this.phoneNumber = phoneNumber;
        this.content = content;
    }

    @Override
    public void validatePayload() {

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
}
