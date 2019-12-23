package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PopupNotification {

    private String contractUuid;

    private String contractCode;

    private Date nextPaymentDate;

    private String status;

    private BigDecimal loanAmount;

    private Date endDue;

    private BigDecimal monthlyInstallmentAmount;

    private Date lastPaymentDate;

    private String loanType;

    private String urlImage;


    public PopupNotification(String contractUuid, String contractCode, Date nextPaymentDate,
                             String status, BigDecimal monthlyInstallmentAmount,BigDecimal loanAmount,Date endDue,
                             Date lastPaymentDate) {
        this.contractUuid = contractUuid;
        this.contractCode = contractCode;
        this.nextPaymentDate = nextPaymentDate;
        this.status = status;
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
        this.loanAmount = loanAmount;
        this.endDue = endDue;
        this.lastPaymentDate = lastPaymentDate;

    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(Date nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(BigDecimal monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getEndDue() {
        return endDue;
    }

    public void setEndDue(Date endDue) {
        this.endDue = endDue;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
