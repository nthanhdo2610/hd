/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.model;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.sms.bean.SMSTemplateUpdate;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 * @author LUUBI
 */
@Entity
@Table(name = "sms_template")
public class SMSTemplate implements HDPayload {

    @Id
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "content", columnDefinition = "VARCHAR(1024)")
    private String content;
    @Column(name = "decription", columnDefinition = "VARCHAR(256)")
    private String decription;
    @Column(name = "sms_type", columnDefinition = "VARCHAR(50)")
    private String smsType;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "created_by")
    private UUID createdBy;
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(name = "modified_by")
    private UUID modifiedBy;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status = 0;
    @Column(name = "lang_code", columnDefinition = "VARCHAR(2)")
    private String langCode;

    public SMSTemplate() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public void validatePayload() {
        //validate content
        if (content == null) {
            throw new BadRequestException(1201, "empty content");
        }

    }

    public void init() {
        this.uuid = UUID.randomUUID();
    }

    public void setSMSTemplateUpdate(SMSTemplateUpdate sMSTemplateUpdate) {
        this.setContent(sMSTemplateUpdate.getContent());
        this.setDecription(sMSTemplateUpdate.getDecription());
        this.setSmsType(sMSTemplateUpdate.getSmsType());
        this.setStatus(sMSTemplateUpdate.getStatus());
        this.setLangCode(sMSTemplateUpdate.getLangCode());
    }

}
