package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

public class RoleListRequest implements HDPayload {
    private int pageNum;
    private int pageSize;
    private String direction;

    public RoleListRequest() {
        this.pageNum = 1;
        this.pageSize = 1;
        this.direction = "ASC";
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
        if (pageNum <= 0)
            throw new BadRequestException(1210, "pageNum invalid");
        if (pageSize <= 0)
            throw new BadRequestException(1211, "pageSize invalid");
    }
}
