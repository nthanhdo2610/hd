package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class MergeFilePdf implements HDPayload {
    private String value;
    private int page;
    private int pointX;
    private int pointY;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    @Override
    public void validatePayload() {

    }
}
