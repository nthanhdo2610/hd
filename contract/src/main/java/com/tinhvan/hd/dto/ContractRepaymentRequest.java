package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;
import java.util.UUID;

public class ContractRepaymentRequest implements HDPayload {
    private String contractUuid;
    private Date paidDate;
    private Double payment;
    private Integer status;


    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractUuid))
            throw new BadRequestException(1406);
        try{
            UUID.fromString(contractUuid);
        } catch(Exception e) {
            throw new BadRequestException(1406);
        }
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
