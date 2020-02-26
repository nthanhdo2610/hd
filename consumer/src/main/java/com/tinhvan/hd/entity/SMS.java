/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 *
 * @author LUUBI
 */
@Entity
@Table(name = "sms")
public class SMS {

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private UUID uuid;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "signature", columnDefinition = "VARCHAR(255)")
    private String signature;

    @Column(name = "otp_code", columnDefinition = "VARCHAR(10)")
    private String otpCode;

    @Column(name = "message", columnDefinition = "VARCHAR(255)")
    private String message;

    @Column(name = "message_id", columnDefinition = "VARCHAR(128)")
    private String messageId;

    @Column(name = "sms_from", columnDefinition = "VARCHAR(128)")
    private String smsFrom;

    @Column(name = "sent_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    @Column(name = "done_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneAt;

    @Column(name = "sms_count")
    private int smsCount = 0;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status = 0;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    public SMS() {
    }

    public SMS(Date date, String phone, String message) {
        this.uuid = UUID.randomUUID();
        this.createdAt = date;
        this.phone = phone;
        this.message = message;
        init();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void init() {
        this.uuid = UUID.randomUUID();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSmsFrom() {
        return smsFrom;
    }

    public void setSmsFrom(String smsFrom) {
        this.smsFrom = smsFrom;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "uuid=" + uuid +
                ", phone='" + phone + '\'' +
                ", signature='" + signature + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                ", smsFrom='" + smsFrom + '\'' +
                ", sentAt=" + sentAt +
                ", doneAt=" + doneAt +
                ", smsCount=" + smsCount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
