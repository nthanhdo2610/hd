package com.tinhvan.hd.dto;

import java.util.Date;

public class ContractWaitingForSigning {

    private String contractCode;

    private String contractUuid;

    private String status;

    private Date contractPrintingDate;

    public ContractWaitingForSigning(String contractCode, String contractUuid, String status, Date contractPrintingDate) {
        this.contractCode = contractCode;
        this.contractUuid = contractUuid;
        this.status = status;
        this.contractPrintingDate = contractPrintingDate;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }
}
