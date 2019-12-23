package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ContractResponseMobile {

    private String contractCode;

    private UUID contractUuid;


    private BigDecimal loanAmount;

    private String loanType;

    private String status;

    // ngay ky hop dong
    private Date contractPrintingDate;

    // ngay thanh toan hang thang
    private Integer monthlyDueDate;

    // ky han
    private Integer tenor;

    private String urlImage;

    private String loanCode;

    private List<String> valueChanges;

    private Date endDate;

    private String statusType;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }


    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }


    public Integer getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Integer monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public List<String> getValueChanges() {
        return valueChanges;
    }

    public void setValueChanges(List<String> valueChanges) {
        this.valueChanges = valueChanges;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}
