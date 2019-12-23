package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentInformation {

    private String contractCode;

    // khoang thanh toan hang thang
    private BigDecimal monthlyInstallmentAmount;

    // ngay thanh toan hang thang
    private Date monthlyDueDate;

    private String loanType;


    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public BigDecimal getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(BigDecimal monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public Date getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Date monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

}
