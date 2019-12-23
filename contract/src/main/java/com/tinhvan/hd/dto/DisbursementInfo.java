package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class DisbursementInfo implements HDPayload {

    private String contractCode;

    private String customerUuid;

    private String bankName;

    private String accountNumber;

    private String brandName;

    private String accountName;

    private Integer isSent;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    @Override
    public String toString() {
        return "DisbursementInfo{" +
                "contractCode='" + contractCode + '\'' +
                ", customerUuid='" + customerUuid + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", brandName='" + brandName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", isSent=" + isSent +
                '}';
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractCode)){
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }
        if(HDUtil.isNullOrEmpty(customerUuid)){
            throw new BadRequestException(1407,"customerUuid  is null or empty !");
        }

        if(HDUtil.isNullOrEmpty(bankName)){
            throw new BadRequestException(1413,"Bank name  is null or empty !");
        }

        if(HDUtil.isNullOrEmpty(accountNumber)){
            throw new BadRequestException(1414,"Account number  is null or empty !");
        }

        if(HDUtil.isNullOrEmpty(brandName)){
            throw new BadRequestException(1415,"Account number  is null or empty !");
        }

        if(HDUtil.isNullOrEmpty(accountName)){
            throw new BadRequestException(1416,"Account number  is null or empty !");
        }
    }
}
