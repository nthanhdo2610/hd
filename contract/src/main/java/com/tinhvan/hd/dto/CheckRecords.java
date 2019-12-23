package com.tinhvan.hd.dto;

import java.util.Date;
import java.util.List;

public class CheckRecords {

    private String contractCode;

    private String status;

    //private String createBy;

    // ngay thanh toan dau tien
    private Date firstDue;

    // ngay ky hop dong
    private Date contractPrintingDate;

    private List<ConfigCheckRecords> config;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(Date firstDue) {
        this.firstDue = firstDue;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }

    public List<ConfigCheckRecords> getConfig() {
        return config;
    }

    public void setConfig(List<ConfigCheckRecords> config) {
        this.config = config;
    }

//    public String getCreateBy() {
//        return createBy;
//    }
//
//    public void setCreateBy(String createBy) {
//        this.createBy = createBy;
//    }
}
