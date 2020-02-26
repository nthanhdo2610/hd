package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_CUSTOMER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_CUSTOMER_SEQ", allocationSize = 1)
public class ContractCustomer {


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
    @Column(name = "STATUS",columnDefinition = "SMALLINT")
    private Integer status;

    @Basic
    @Column(name = "IS_SIGN_UP",columnDefinition = "SMALLINT")
    private Integer isSignUp;

    // default 1
    @Basic
    @Column(name = "IS_REPAYMENT_NOTIFICATION",columnDefinition = "SMALLINT")
    private Integer isRepaymentNotification;


    @Basic
    @Column(name = "CUSTOMER_UUID")
    private UUID customerUuid;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;


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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getIsRepaymentNotification() {
        return isRepaymentNotification;
    }

    public void setIsRepaymentNotification(Integer isRepaymentNotification) {
        this.isRepaymentNotification = isRepaymentNotification;
    }

    public Integer getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(Integer isSignUp) {
        this.isSignUp = isSignUp;
    }
}
