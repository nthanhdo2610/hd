package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SearchRegisterByPhoneRequest implements HDPayload {
    private String keyWord="";
    private Date dateFrom;
    private Date dateTo;
    private int pageNum;
    private int pageSize;
    private String orderBy="modifiedAt";
    private String direction="desc";

    @Override
    public void validatePayload() {
        if(pageNum <= 0)
            pageNum = 1;
        if(pageSize <= 0)
            pageSize = 10;
        if (dateFrom != null) {
            dateFrom = HDUtil.setBeginDay(dateFrom);
        }
        if (dateTo != null) {
            dateTo = HDUtil.setEndDay(dateTo);
        }
        if(HDUtil.isNullOrEmpty(orderBy))
            orderBy="modifiedAt";
        if(HDUtil.isNullOrEmpty(direction))
            direction="desc";
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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
