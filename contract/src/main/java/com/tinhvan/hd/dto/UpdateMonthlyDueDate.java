package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;

public class UpdateMonthlyDueDate implements HDPayload {

    private String contractCode;

    private Integer monthlyDueDate;

    private Date firstDate;

    private Date endDate;

    @Override
    public String toString() {
        return "UpdateMonthlyDueDate{" +
                "contractCode='" + contractCode + '\'' +
                ", monthlyDueDate=" + monthlyDueDate +
                '}';
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Integer monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(contractCode)) {
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }

        if (monthlyDueDate == null || monthlyDueDate < 1) {
            throw new BadRequestException(1426,"MonthlyDueDate is null or empty !");
        }
    }
}
