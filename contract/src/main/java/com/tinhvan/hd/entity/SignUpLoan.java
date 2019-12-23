package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "SIGN_UP_LOAN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SIGN_UP_LOAN_SEQ", allocationSize = 1)
public class SignUpLoan implements Cloneable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "FULL_NAME")
    private String fullName;

    @Basic
    @Column(name = "PHONE")
    private String phone;

    @Basic
    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Basic
    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Basic
    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Basic
    @Column(name = "LOAN_TYPE")
    private String loanType;

    @Basic
    @Column(name = "PRODUCTION_ID")
    private Integer productionId;

    // khoan cap von
    @Basic
    @Column(name = "LOAN_AMOUNT")
    private BigDecimal loanAmount;

    // ky han (bao nhieu thang)
    @Basic
    @Column(name = "TENOR")
    private Integer tenor;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Integer getProductionId() {
        return productionId;
    }

    public void setProductionId(Integer productionId) {
        this.productionId = productionId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

}
