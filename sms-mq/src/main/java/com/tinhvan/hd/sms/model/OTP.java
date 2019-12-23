/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author LUUBI
 */
@Entity(name = "otp")
public class OTP{

    @Id
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    private UUID uuid;
    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;
    @Column(name = "otp_code", columnDefinition = "VARCHAR(10)")
    private String otpCode;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(name = "app_id")
    private String appID;
    @Column(name = "customer_uuid", columnDefinition = "BINARY(16)")
    private UUID customerUUID;
    @Column(name = "contract_uuid", columnDefinition = "BINARY(16)")
    private UUID contractUUID;
    @Column(name = "type", columnDefinition = "SMALLINT")
    private int type;
    @Column(name = "expired_time")
    private int expiredTime;
    @Column(name = "process_time")
    private long processTime;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status;

    public OTP() {
    }

    public OTP(String phone, Date createdAt, String appID, int type, String otpCode, int smsExpiredTime, UUID customerUUID, UUID contractUUID) {
        this.uuid = UUID.randomUUID();
        this.phone = phone;
        this.createdAt = createdAt;
        this.appID = appID;
        this.type = type;
        this.otpCode = otpCode;
        this.expiredTime = smsExpiredTime;
        this.customerUUID = customerUUID;
        this.contractUUID = contractUUID;
        this.processTime = createdAt.getTime();
        this.status = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public UUID getCustomerUUID() {
        return customerUUID;
    }

    public void setCustomerUUID(UUID customerUUID) {
        this.customerUUID = customerUUID;
    }

    public UUID getContractUUID() {
        return contractUUID;
    }

    public void setContractUUID(UUID contractUUID) {
        this.contractUUID = contractUUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void init() {
        this.uuid = UUID.randomUUID();
    }

    public String generatedOTP(int len) {

        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }


}
