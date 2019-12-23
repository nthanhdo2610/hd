package com.tinhvan.hd.sms.invoke;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


public class ContractEsigned implements HDPayload {

    private Integer id;

    private UUID contractUuid;

    private Integer isSigned;

    private String otpCode;

    private UUID createdBy;

    private Date createdAt;

    private String esignedPhone;

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

    public Integer getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(Integer isSigned) {
        this.isSigned = isSigned;
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

    @Override
    public void validatePayload() {

    }
}
