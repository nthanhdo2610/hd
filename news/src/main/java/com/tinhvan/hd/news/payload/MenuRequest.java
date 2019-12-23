package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.news.entity.News;

import java.util.UUID;

public class MenuRequest implements HDPayload {

    private String customerUuid;
    private int access;
    int type;
    private String keyWord;
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private String direction;

    @Override
    public void validatePayload() {
        if (!HDUtil.isNullOrEmpty(customerUuid)) {
            try {
                UUID.fromString(customerUuid);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }
        }
        if (access != News.ACCESS.GENERAL && access != News.ACCESS.INDIVIDUAL)
            access = 0;
        if (type < 0)
            type = 0;
        if(pageNum <= 0)
            pageNum = 1;
        if(pageSize <= 0)
            pageSize = 10;
        if(keyWord==null)
            keyWord = "";
        if(orderBy==null)
            orderBy = "";
        if(direction==null)
            direction = "";
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "MenuRequest{" +
                "customerUuid='" + customerUuid + '\'' +
                ", access=" + access +
                ", type=" + type +
                ", keyWord='" + keyWord + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
