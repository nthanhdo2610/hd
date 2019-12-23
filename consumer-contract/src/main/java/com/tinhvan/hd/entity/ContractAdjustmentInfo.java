package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_ADJUSTMENT_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_ADJUSTMENT_INFO_SEQ", allocationSize = 1)
public class ContractAdjustmentInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Basic
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Basic
    @Column(name = "LAST_NAME")
    private String lastName;


    @Basic
    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Basic
    @Column(name = "IDENTIFY_ID",columnDefinition = "char")
    private String identifyId;


    @Basic
    @Column(name = "IDENTIFY_ID_DATE")
    private Date identifyIdDate;

    @Basic
    @Column(name = "PERMANENT_ADDRESS",length = 128)
    private String permanentAddress;


    @Basic
    @Column(name = "TOTAL_VALUE",columnDefinition = "SMALLINT")
    private Integer totalValue;

    @Basic
    @Column(name = "PREPAID_MONEY",columnDefinition = "SMALLINT")
    private Integer prepaidMoney;

    @Basic
    @Column(name = "LOAN_REQUEST",columnDefinition = "SMALLINT")
    private Integer loanRequest;


    @Basic
    @Column(name = "STORE_NAME",length = 256)
    private String storeName;

    @Basic
    @Column(name = "STORE_ADDRESS",length = 256)
    private String storeAddress;


    @Basic
    @Column(name = "FISRST_PAYMENT_DATE")
    private Date firstPaymentDate;

    @Basic
    @Column(name = "PRODUCT_NAME",length = 256)
    private String productName;

    @Basic
    @Column(name = "FRAME_OR_SERIAL_NUMBER",length = 20)
    private String frameOrSerialNumber;


    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Basic
    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;

    @Basic
    @Column(name = "IS_DELETED",columnDefinition = "SMALLINT")
    private Integer isDeleted;


    @Basic
    @Column(name = "DELETED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Basic
    @Column(name = "DELETED_BY")
    private UUID deletedBy;

    @Basic
    @Column(name = "IS_APPROVED",columnDefinition = "SMALLINT")
    private Integer isApproved;

    @Basic
    @Column(name = "IS_ESIGNED",columnDefinition = "SMALLINT")
    private Integer isEsigned;




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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public Date getIdentifyIdDate() {
        return identifyIdDate;
    }

    public void setIdentifyIdDate(Date identifyIdDate) {
        this.identifyIdDate = identifyIdDate;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Integer getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Integer totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getPrepaidMoney() {
        return prepaidMoney;
    }

    public void setPrepaidMoney(Integer prepaidMoney) {
        this.prepaidMoney = prepaidMoney;
    }

    public Integer getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(Integer loanRequest) {
        this.loanRequest = loanRequest;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Date getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(Date firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFrameOrSerialNumber() {
        return frameOrSerialNumber;
    }

    public void setFrameOrSerialNumber(String frameOrSerialNumber) {
        this.frameOrSerialNumber = frameOrSerialNumber;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getIsEsigned() {
        return isEsigned;
    }

    public void setIsEsigned(Integer isEsigned) {
        this.isEsigned = isEsigned;
    }
}
