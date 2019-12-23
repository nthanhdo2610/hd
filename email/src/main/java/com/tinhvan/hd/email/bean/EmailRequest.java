package com.tinhvan.hd.email.bean;

import com.tinhvan.hd.base.HDPayload;

public class EmailRequest implements HDPayload {

    //input values
    private String emailType;

    private String langCode;

    private String[] listEmail;

    private String[] listFile;

    private String[] params;

    //data convert
    private String userName;

    private String password;

    private String mailFrom;

    private String fileType;

    private String title;

    private String content;

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    }
}
