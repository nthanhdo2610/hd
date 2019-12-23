/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.model;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.email.bean.EmailTemplateUpdate;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 *
 * @author LUUBI
 */
@Entity
@Table(name = "email_template")
public class EmailTemplate implements HDPayload {

    @Id
    @SequenceGenerator(name = "email_template_sequence", sequenceName = "email_template_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_template_sequence")
    @Column(name = "id")
    private int id;
    @Column(name = "title", columnDefinition = "VARCHAR(160)")
    private String title;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "email_type", columnDefinition = "VARCHAR(100)")
    private String emailType;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "create_by")
    private UUID createBy;
    @Column(name = "modified_at")
    private Date modifiedAt;
    @Column(name = "modified_by")
    private UUID modifiedBy;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status = 0;
    @Column(name = "lang_code", columnDefinition = "VARCHAR(2)")
    private String langCode;

    public EmailTemplate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }



    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public UUID getCreateBy() {
        return createBy;
    }

    public void setCreateBy(UUID createBy) {
        this.createBy = createBy;
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

    @Override
    public void validatePayload() {
        //validate content
//        if (content == null) {
//            throw new BadRequestException(1201, "empty content");
//        }
    }

    public void setEmailTemplateUpdate(EmailTemplateUpdate emailTemplateUpdate) {
        this.setContent(emailTemplateUpdate.getContent());
        this.setEmailType(emailTemplateUpdate.getEmailType());
        this.setStatus(emailTemplateUpdate.getStatus());
        this.setLangCode(emailTemplateUpdate.getLangCode());
    }
}
