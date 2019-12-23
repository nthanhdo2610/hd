package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ContractAdjustmentResponse {

    private String contractCode;

    private String fullName;

    private BigDecimal loanAmount;

    private String loanType;

    private String status;

    private Date dateUpdateContract;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Date getDateUpdateContract() {
        return dateUpdateContract;
    }

    public void setDateUpdateContract(Date dateUpdateContract) {
        this.dateUpdateContract = dateUpdateContract;
    }
}
