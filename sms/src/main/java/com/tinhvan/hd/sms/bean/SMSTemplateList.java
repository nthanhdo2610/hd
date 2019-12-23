package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.HDPayload;

public class SMSTemplateList implements HDPayload {
    private int pageNum;
    private int pageSize;
    private String direction;

    public SMSTemplateList(int pageNum, int pageSize, String direction) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.direction = direction;
    }

    public SMSTemplateList() {
        this.pageNum = 0;
        this.pageSize = 0;
        this.direction = "DESC";
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public void validatePayload() {

    }
}
