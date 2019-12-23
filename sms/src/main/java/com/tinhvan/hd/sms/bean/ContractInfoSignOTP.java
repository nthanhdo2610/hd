package com.tinhvan.hd.sms.bean;

import java.util.Date;

public class ContractInfoSignOTP {

    private int id;
    private String contractCode;
    private String customerUuid;
    private String bankName;
    private String accountNumber;
    private String brandName;
    private String accountName;
    private int isSent;
    private Date createAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "ContractInfoSignOTP{" +
                "id=" + id +
                ", contractCode='" + contractCode + '\'' +
                ", customerUuid='" + customerUuid + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", brandName='" + brandName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", isSent=" + isSent +
                ", createAt=" + createAt +
                '}';
    }
}
