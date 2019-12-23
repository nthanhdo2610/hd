package com.tinhvan.hd.email.bean;

public class ContentEmail {
    private String fileType;
    private String title;
    private String content;

    public ContentEmail(String fileType, String title, String content) {
        this.fileType = fileType;
        this.title = title;
        this.content = content;
    }

    public ContentEmail() {
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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
}
