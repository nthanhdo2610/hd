package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;
import java.util.UUID;

public class ContractLoanRequest implements HDPayload {

    private int id;
    private String contractUuid;
    private Integer duration;
    private Integer interestRate;
    private Integer loanAmount;
    private Integer remainingDebt;
    private Date monthlyDueDate;
    private Date remainingDebtModifiedAt;
    private Integer isReminder;
    private String loanCategory;
    private Integer monthlyInstallmentAmount;
    private String insuranceCategory;
    private Date startDate;
    private Date finishDate;



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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getRemainingDebt() {
        return remainingDebt;
    }

    public void setRemainingDebt(Integer remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    public Date getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Date monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public Date getRemainingDebtModifiedAt() {
        return remainingDebtModifiedAt;
    }

    public void setRemainingDebtModifiedAt(Date remainingDebtModifiedAt) {
        this.remainingDebtModifiedAt = remainingDebtModifiedAt;
    }

    public Integer getIsReminder() {
        return isReminder;
    }

    public void setIsReminder(Integer isReminder) {
        this.isReminder = isReminder;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public Integer getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(Integer monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public String getInsuranceCategory() {
        return insuranceCategory;
    }

    public void setInsuranceCategory(String insuranceCategory) {
        this.insuranceCategory = insuranceCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
