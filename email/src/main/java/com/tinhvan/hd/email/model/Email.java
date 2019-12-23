/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.model;

import com.tinhvan.hd.base.HDPayload;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author LUUBI
 */
@Entity
@Table(name = "email")
public class Email implements HDPayload{
    @Id
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "email", columnDefinition = "VARCHAR(20)")
    private String email;
    @Column(name = "signature", columnDefinition = "VARCHAR(255)")
    private String signature;
    @Column(name = "otp_code", columnDefinition = "VARCHAR(10)")
    private String otpCode;
    @Column(name = "message", columnDefinition = "VARCHAR(255)")
    private String message;
    @Column(name = "subject", columnDefinition = "VARCHAR(255)")
    private String subject;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public void validatePayload() {
    }
    
    public void init(){
        this.uuid = UUID.randomUUID();
    }
    
    
}
