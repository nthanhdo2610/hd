package com.tinhvan.hd.dto;

import java.util.Date;

public class PhoneAndStatus {
    private String contractNumber;

    private String phoneNumber;

    private String status;

    private Date lastUpdatePhone;

    private String  documentVerificationDate;

    private String contractPrintingDate;

    private Date matureDate;

    private Date earlyDate;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdatePhone() {
        return lastUpdatePhone;
    }

    public void setLastUpdatePhone(Date lastUpdatePhone) {
        this.lastUpdatePhone = lastUpdatePhone;
    }


    public String getDocumentVerificationDate() {
        return documentVerificationDate;
    }

    public void setDocumentVerificationDate(String documentVerificationDate) {
        this.documentVerificationDate = documentVerificationDate;
    }

    public String getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(String contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }

    public Date getMatureDate() {
        return matureDate;
    }

    public void setMatureDate(Date matureDate) {
        this.matureDate = matureDate;
    }

    public Date getEarlyDate() {
        return earlyDate;
    }

    public void setEarlyDate(Date earlyDate) {
        this.earlyDate = earlyDate;
    }
}
