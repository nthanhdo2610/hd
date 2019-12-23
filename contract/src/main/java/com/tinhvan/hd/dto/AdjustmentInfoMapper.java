package com.tinhvan.hd.dto;

import java.util.Date;
import java.util.UUID;

public class AdjustmentInfoMapper {

    private String contractCode;

    private Date createdAt;


    public AdjustmentInfoMapper(String contractCode, Date createdAt) {
        this.contractCode = contractCode;
        this.createdAt = createdAt;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
