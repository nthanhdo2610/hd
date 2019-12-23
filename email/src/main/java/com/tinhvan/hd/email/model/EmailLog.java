/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author LUUBI
 */
@Entity(name = "email_log")
public class EmailLog {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @Column(name = "uuid_mail")
    private UUID uuidMail;
    @Column(name = "uuid_mail_template")
    private UUID uuidMailTemplate;
    @Column(name = "crate_by")
    private UUID crateBy;
    @Column(name = "fault")
    private String fault;
    @Column(name = "create_at")
    private Date createAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUuidMail() {
        return uuidMail;
    }

    public void setUuidMail(UUID uuidMail) {
        this.uuidMail = uuidMail;
    }

    public UUID getUuidMailTemplate() {
        return uuidMailTemplate;
    }

    public void setUuidMailTemplate(UUID uuidMailTemplate) {
        this.uuidMailTemplate = uuidMailTemplate;
    }

    public UUID getCrateBy() {
        return crateBy;
    }

    public void setCrateBy(UUID crateBy) {
        this.crateBy = crateBy;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
    
}
