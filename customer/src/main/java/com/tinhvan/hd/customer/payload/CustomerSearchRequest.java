package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class CustomerSearchRequest implements HDPayload {

    private int type;
    private String keyWord="";
    private int pageNum;
    private int pageSize;
    private String orderBy="";
    private String direction="";

    public static final class TYPE {
        public static final int ALL = 0;
        public static final int IDENTITY = 1;
        public static final int CONTACT = 2;
    }

    @Override
    public void validatePayload() {
        if(type < 0)
            type = 0;
        if(pageNum <= 0)
            pageNum = 1;
        if(pageSize <= 0)
            pageSize = 10;
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
        return "CustomerSearchRequest{" +
                "type=" + type +
                ", keyWord='" + keyWord + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
