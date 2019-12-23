package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_REPAYMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_REPAYMENT_SEQ", allocationSize = 1)
public class ContractRepayment implements HDPayload,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_CODE")
    private String contractCode;


    @Basic
    @Column(name = "PAID_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;

    @Basic
    @Column(name = "PAYMENT")
    private BigDecimal payment;

    @Basic
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Basic
    @Column(name = "STATUS",columnDefinition = "SMALLINT",length = 16)
    private Integer status;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public void validatePayload() {

    }
}
