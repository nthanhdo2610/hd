package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;

public class PromotionSearchRequest implements HDPayload {

    private int access;
    private String type;
    private String keyWord;
    private Date dateFrom;
    private Date dateTo;
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private String direction;

    @Override
    public void validatePayload() {
        if(access < 0)
            access = 0;
        if(pageNum <= 0)
            pageNum = 1;
        if(pageSize <= 0)
            pageSize = 10;
        if(type==null)
            type = "";
        if(keyWord==null)
            keyWord = "";
        if(HDUtil.isNullOrEmpty(orderBy))
            orderBy = "createdAt";
        if(HDUtil.isNullOrEmpty(direction))
            direction = "desc";
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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
}
