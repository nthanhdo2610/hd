package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;
import java.util.UUID;

public class PaymentInfoRequest implements HDPayload {

    private UUID customerUuid;

    private String contractCode;

    private Date latestPaymentDate;

    private String loanType;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getLatestPaymentDate() {
        return latestPaymentDate;
    }

    public void setLatestPaymentDate(Date latestPaymentDate) {
        this.latestPaymentDate = latestPaymentDate;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return "PaymentInfoRequest{" +
                "customerUuid=" + customerUuid +
                ", contractCode='" + contractCode + '\'' +
                ", latestPaymentDate=" + latestPaymentDate +
                '}';
    }

    @Override
    public void validatePayload() {
        if(customerUuid == null || HDUtil.isNullOrEmpty(customerUuid.toString())){
            throw new BadRequestException(1403,"Contract code is null or empty !");
        }
    }
}
