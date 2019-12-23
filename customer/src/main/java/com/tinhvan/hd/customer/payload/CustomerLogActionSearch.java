package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;

import java.util.Date;
import java.util.UUID;

public class CustomerLogActionSearch implements HDPayload {

    private UUID customerId;
    private String contractCode;
    private String objectName;
    private Date fromDate;
    private Date toDate;
    private int pageNum;
    private int pageSize;
    private String direction;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    @Override
    public String toString() {
        return "CustomerLogActionSearch{" +
                "customerId=" + customerId +
                ", contractCode='" + contractCode + '\'' +
                ", objectName='" + objectName + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", direction='" + direction + '\'' +
                '}';
    }
}
