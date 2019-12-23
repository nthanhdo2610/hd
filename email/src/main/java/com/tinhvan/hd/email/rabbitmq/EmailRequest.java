/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.rabbitmq;

import com.tinhvan.hd.base.HDPayload;

import java.util.Arrays;

/**
 * @author LUUBI
 */
public class EmailRequest implements HDPayload {

    private String emailType;

    private String langCode;

    private String[] params;

    private String title;

    private String[] listEmail;

    private String[] listFile;

    private String content;

    private String userName;

    private String password;

    private String mailFrom;

    private String fileType;

    public EmailRequest() {
    }

    public EmailRequest(String[] listEmail, String[] listFile, String title, String content, String fileType) {
        this.listEmail = listEmail;
        this.listFile = listFile;
        this.title = title;
        this.content = content;
        this.fileType = fileType;
    }

    public String[] getListEmail() {
        return listEmail;
    }

    public void setListEmail(String[] listEmail) {
        this.listEmail = listEmail;
    }

    public String[] getListFile() {
        return listFile;
    }

    public void setListFile(String[] listFile) {
        this.listFile = listFile;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public void validatePayload() {
        if (fileType == null)
            fileType = "";
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "emailType='" + emailType + '\'' +
                ", langCode='" + langCode + '\'' +
                ", title='" + title + '\'' +
                ", listEmail=" + Arrays.toString(listEmail) +
                ", content='" + content + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
