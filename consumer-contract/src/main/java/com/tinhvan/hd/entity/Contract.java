package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_SEQ", allocationSize = 1)
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "LENDINGCORE_CONTRACT_ID",length = 128)
    private String lendingCoreContractId;

    @Basic
    @Column(name = "PHONE",length = 20)
    private String phone;

    @Basic
    @Column(name = "IDENTIFY_ID",length = 20)
    private String identifyId;


    @Basic
    @Column(name = "STATUS",columnDefinition = "SMALLINT")
    private Integer status;


    @Basic
    @Column(name = "CUSTOMER_UUID")
    private UUID customerUuid;


    @Basic
    @Column(name = "IS_REMOVE",columnDefinition = "SMALLINT")
    private Integer isRemove = 0;

    @Basic
    @Column(name = "IS_ESIGN",columnDefinition = "SMALLINT")
    private Integer isEsign = 1;

    @Basic
    @Column(name = "PHONE_ESIGN",length = 20)
    private String phoneEsign;

    @Basic
    @Column(name = "CONTRACT_COMPLETE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractCompleteDate;

    @Basic
    @Column(name = "DOCUMENT_VERIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentVerificationDate;

    @Basic
    @Column(name = "CONTRACT_PRINTING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractPrintingDate;

    @Basic
    @Column(name = "LOAN_AMOUNT")
    private Double loanAmount;

    @Basic
    @Column(name = "DUREATION",columnDefinition = "SMALLINT")
    private Integer duration;

    @Basic
    @Column(name = "MONTHLY_INSTALLMENT_AMOUNT")
    private Double monthlyInstallmentAmount;

    @Basic
    @Column(name = "MONTHLY_DUE_DATE")
    private Date monthlyDueDate;


    @Basic
    @Column(name = "CONTRACT_FINISH_DATE")
    private Date contractFinishDate;

    @Basic
    @Column(name = "IS_INSURANCE",columnDefinition = "SMALLINT")
    private Integer isInsurance;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;



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


    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
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



    public Date getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Date monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }



    public Double getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(Double monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public String getLendingCoreContractId() {
        return lendingCoreContractId;
    }

    public void setLendingCoreContractId(String lendingCoreContractId) {
        this.lendingCoreContractId = lendingCoreContractId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Integer getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Integer isRemove) {
        this.isRemove = isRemove;
    }

    public Integer getIsEsign() {
        return isEsign;
    }

    public void setIsEsign(Integer isEsign) {
        this.isEsign = isEsign;
    }

    public String getPhoneEsign() {
        return phoneEsign;
    }

    public void setPhoneEsign(String phoneEsign) {
        this.phoneEsign = phoneEsign;
    }

    public Date getContractCompleteDate() {
        return contractCompleteDate;
    }

    public void setContractCompleteDate(Date contractCompleteDate) {
        this.contractCompleteDate = contractCompleteDate;
    }

    public Date getDocumentVerificationDate() {
        return documentVerificationDate;
    }

    public void setDocumentVerificationDate(Date documentVerificationDate) {
        this.documentVerificationDate = documentVerificationDate;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getContractFinishDate() {
        return contractFinishDate;
    }

    public void setContractFinishDate(Date contractFinishDate) {
        this.contractFinishDate = contractFinishDate;
    }

    public Integer getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(Integer isInsurance) {
        this.isInsurance = isInsurance;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
