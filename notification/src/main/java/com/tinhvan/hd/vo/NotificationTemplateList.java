package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.HDPayload;

public class NotificationTemplateList implements HDPayload {
    private int pageNum;
    private int pageSize;
    private String direction;

    public NotificationTemplateList(int pageNum, int pageSize, String direction) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.direction = direction;
    }

    public NotificationTemplateList() {
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
