package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_ESIGNED")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_ESIGNED_SEQ", allocationSize = 1)
public class ContractEsigned {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "CONTRACT_CODE",length = 128)
    private String contractCode;

    @Basic
    @Column(name = "IS_SIGNED_ADJUSTMENT",columnDefinition = "SMALLINT",length = 16)
    private Integer isSignedAdjustment; // isSigned_adjustment: 1: Signed, 2: adjustment

    @Basic
    @Column(name = "OTP",columnDefinition = "char", length = 20)
    private String otpCode;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "esigned_phone",length = 20)
    private String esignedPhone;

    @Basic
    @Column(name = "contract_type")
    private String contractType;

    @Basic
    @Column(name = "environment",length = 10)
    private String environment;

    @Basic
    @Column(name = "contract_printing_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractPrintingDate;

    @Basic
    @Column(name = "esigned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eSignedAt;

    @Basic
    @Column(name = "contract_file_created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractFileCreatedAt;

    @Basic
    @Column(name = "customer_uuid")
    private UUID customerUuid;

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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getIsSignedAdjustment() {
        return isSignedAdjustment;
    }

    public void setIsSignedAdjustment(Integer isSignedAdjustment) {
        this.isSignedAdjustment = isSignedAdjustment;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEsignedPhone() {
        return esignedPhone;
    }

    public void setEsignedPhone(String esignedPhone) {
        this.esignedPhone = esignedPhone;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }

    public Date geteSignedAt() {
        return eSignedAt;
    }

    public void seteSignedAt(Date eSignedAt) {
        this.eSignedAt = eSignedAt;
    }

    public Date getContractFileCreatedAt() {
        return contractFileCreatedAt;
    }

    public void setContractFileCreatedAt(Date contractFileCreatedAt) {
        this.contractFileCreatedAt = contractFileCreatedAt;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }
}
