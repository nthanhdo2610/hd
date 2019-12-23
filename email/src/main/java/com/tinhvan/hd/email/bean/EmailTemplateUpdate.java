/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public class EmailTemplateUpdate implements HDPayload{
    private int id;
    private String content;
    private String emailType;
    private int status;
    private String langCode;

    public EmailTemplateUpdate() {
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

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
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
    public void validatePayload() {//validate uuid
        if (id == 0) {
            throw new BadRequestException(1200, "empty uuid");
        }
    }
    
}
