package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_LOAN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_LOAN_SEQ", allocationSize = 1)
public class ContractLoan {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "DURATION")
    private Integer duration;

    @Basic
    @Column(name = "INTEREST_RATE",columnDefinition = "SMALLINT")
    private Integer interestRate;

    @Basic
    @Column(name = "LOAN_AMOUNT",columnDefinition = "SMALLINT")
    private Integer loanAmount;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;


    @Basic
    @Column(name = "REMAINING_DEPT",columnDefinition = "SMALLINT")
    private Integer remainingDebt;


    @Basic
    @Column(name = "MONTHLY_DUE_DATE")
    private Date monthlyDueDate;


    @Basic
    @Column(name = "REMAINING_DEPT_MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remainingDebtModifiedAt;


    @Basic
    @Column(name = "IS_REMINDER",columnDefinition = "SMALLINT")
    private Integer isReminder;

    @Basic
    @Column(name = "LOAN_CATEGORY")
    private String loanCategory;

    @Basic
    @Column(name = "MONTHLY_INSTALLMENT_AMOUNT",columnDefinition = "SMALLINT")
    private Integer monthlyInstallmentAmount;


    @Basic
    @Column(name = "INSURANCE_CATEGORY")
    private String insuranceCategory;

    @Basic
    @Column(name = "START_DATE")
    private Date startDate;

    @Basic
    @Column(name = "FINISH_DATE")
    private Date finishDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
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
