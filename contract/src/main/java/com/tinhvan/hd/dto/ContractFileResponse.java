package com.tinhvan.hd.dto;

public class ContractFileResponse {
    private String fileLink;
    private long fileSize;
    private int fileCountPage;

    public ContractFileResponse() {
        super();
    }

    public ContractFileResponse(String fileLink, long fileSize, int fileCountPage) {
        this.fileLink = fileLink;
        this.fileSize = fileSize;
        this.fileCountPage = fileCountPage;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileCountPage() {
        return fileCountPage;
    }

    public void setFileCountPage(int fileCountPage) {
        this.fileCountPage = fileCountPage;
    }
}
