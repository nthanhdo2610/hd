package com.tinhvan.hd.sms.bean;

import java.util.Date;
import java.util.UUID;

public class OTPVerifyResult {
    private UUID uuid;
    private String otp_type;
    private String otp_code;
    private int status;
    private long process_time;
    private Date createdAt;
    private Date currentDate;



    public OTPVerifyResult(UUID uuid, String otp_type, String otp_code, int status, long process_time, Date createdAt, Date currentDate) {
        this.uuid = uuid;
        this.otp_type = otp_type;
        this.otp_code = otp_code;
        this.status = status;
        this.process_time = process_time;
        this.createdAt = createdAt;
        this.currentDate = currentDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOtp_type() {
        return otp_type;
    }

    public void setOtp_type(String otp_type) {
        this.otp_type = otp_type;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProcess_time() {
        return process_time;
    }

    public void setProcess_time(long process_time) {
        this.process_time = process_time;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
