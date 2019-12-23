package com.tinhvan.hd.entity;
import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_ADJUSTMENT_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_ADJUSTMENT_INFO_SEQ", allocationSize = 1)
public class ContractAdjustmentInfo implements HDPayload {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_CODE")
    private String contractCode;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "STR_CREATED_AT")
    private String strCreatedAt;

    @Basic
    @Column(name = "KEY")
    private String key;

    @Basic
    @Column(name = "VALUE")
    private String value;

    @Basic
    @Column(name = "VALUE_CONFIRM")
    private String valueConfirm;

    @Basic
    @Column(name = "CREATED_CONFIRM_BY")
    private UUID createdConfirmBy;

    @Basic
    @Column(name = "CREATED_CONFIRM_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdConfirmAt;

    @Basic
    @Column(name = "STR_CREATED_CONFIRM_AT")
    private String strCreatedConfirmAt;

    @Basic
    @Column(name = "IS_CONFIRM")
    private Integer isConfirm;

    @Basic
    @Column(name = "IS_SENT_MAIL")
    private Integer isSentMail;

    @Basic
    @Column(name = "SENT_MAIL_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentMailAt;





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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueConfirm() {
        return valueConfirm;
    }

    public void setValueConfirm(String valueConfirm) {
        this.valueConfirm = valueConfirm;
    }

    public UUID getCreatedConfirmBy() {
        return createdConfirmBy;
    }

    public void setCreatedConfirmBy(UUID createdConfirmBy) {
        this.createdConfirmBy = createdConfirmBy;
    }

    public Date getCreatedConfirmAt() {
        return createdConfirmAt;
    }

    public void setCreatedConfirmAt(Date createdConfirmAt) {
        this.createdConfirmAt = createdConfirmAt;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getStrCreatedAt() {
        return strCreatedAt;
    }

    public void setStrCreatedAt(String strCreatedAt) {
        this.strCreatedAt = strCreatedAt;
    }

    public String getStrCreatedConfirmAt() {
        return strCreatedConfirmAt;
    }

    public void setStrCreatedConfirmAt(String strCreatedConfirmAt) {
        this.strCreatedConfirmAt = strCreatedConfirmAt;
    }

    public Integer getIsSentMail() {
        return isSentMail;
    }

    public void setIsSentMail(Integer isSentMail) {
        this.isSentMail = isSentMail;
    }

    public Date getSentMailAt() {
        return sentMailAt;
    }

    public void setSentMailAt(Date sentMailAt) {
        this.sentMailAt = sentMailAt;
    }

    @Override
    public void validatePayload() {

    }
}
