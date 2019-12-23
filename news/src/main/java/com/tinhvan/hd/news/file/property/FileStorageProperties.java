package com.tinhvan.hd.news.file.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String logDir;
    private long logExpired;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public long getLogExpired() {
        return logExpired;
    }

    public void setLogExpired(long logExpired) {
        this.logExpired = logExpired;
    }
}