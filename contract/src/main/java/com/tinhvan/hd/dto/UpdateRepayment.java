package com.tinhvan.hd.dto;

import java.util.Date;

public class UpdateRepayment {

    private String contractCode;

    private String status;

    private Date lastSycnRePaymentDate;

    private Date paidDateMax;

    private Double paymentTotal;


    public UpdateRepayment(String contractCode, String status, Date lastSycnRePaymentDate, Date paidDateMax, Double paymentTotal) {
        this.contractCode = contractCode;
        this.status = status;
        this.lastSycnRePaymentDate = lastSycnRePaymentDate;
        this.paidDateMax = paidDateMax;
        this.paymentTotal = paymentTotal;
    }

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

    public Date getLastSycnRePaymentDate() {
        return lastSycnRePaymentDate;
    }

    public void setLastSycnRePaymentDate(Date lastSycnRePaymentDate) {
        this.lastSycnRePaymentDate = lastSycnRePaymentDate;
    }

    public Date getPaidDateMax() {
        return paidDateMax;
    }

    public void setPaidDateMax(Date paidDateMax) {
        this.paidDateMax = paidDateMax;
    }

    public Double getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(Double paymentTotal) {
        this.paymentTotal = paymentTotal;
    }
}
