package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;

public class NewsSearchRequest implements HDPayload {

    private int access;
    private int type;
    private Date dateFrom;
    private Date dateTo;
    private String keyWord;
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private String direction;

    @Override
    public void validatePayload() {
        if(type < 0)
            type = 0;
        if(access < 0)
            access = 0;
        if(pageNum <= 0)
            pageNum = 1;
        if(pageSize <= 0)
            pageSize = 10;
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
}
