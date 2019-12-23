package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class AdjustmentUploadFileSearch implements HDPayload {

    private String keyWord;
    private Date dateFrom;
    private Date dateTo;
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private String direction;

    @Override
    public void validatePayload() {
        if(dateFrom!=null){
            dateFrom = DateUtils.setHours(dateFrom, 0);
            dateFrom = DateUtils.setMinutes(dateFrom, 0);
            dateFrom = DateUtils.setSeconds(dateFrom, 0);
        }
        if(dateTo!=null){
            dateTo = DateUtils.setHours(dateTo, 23);
            dateTo = DateUtils.setMinutes(dateTo, 59);
            dateTo = DateUtils.setSeconds(dateTo, 59);
        }
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
