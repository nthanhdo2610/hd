package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.math.BigDecimal;

public class SignUpLoanRequest implements HDPayload {

    private String fullName;
    private String phone;
    private String nationalId;
    private String provinceName;
    private String districtName;
    private String loanType;
    private String productionName;
    private BigDecimal loanAmount;

    private int tenor;

    private int interestRate;

    private int percentPrepaid;

    private int monthlyInstallmentAmount;

    private String customerUuid;

    @Override
    public String toString() {
        return "SignUpLoanRequest{" +
                "fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", loanType='" + loanType + '\'' +
                ", productionName='" + productionName + '\'' +
                ", loanAmount=" + loanAmount +
                ", tenor=" + tenor +
                ", interestRate=" + interestRate +
                ", percentPrepaid=" + percentPrepaid +
                ", monthlyInstallmentAmount=" + monthlyInstallmentAmount +
                ", customerUuid='" + customerUuid + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(fullName))
            throw new BadRequestException(1203);
        if(fullName.length()>255)
            throw new BadRequestException(1100);
        if(!HDUtil.isPhoneNumber(phone))
            throw new BadRequestException(1237);

        if (loanAmount == null) {
            throw new BadRequestException(1438,"Vui lòng nhập số tiền");
        }
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



    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public int getPercentPrepaid() {
        return percentPrepaid;
    }

    public void setPercentPrepaid(int percentPrepaid) {
        this.percentPrepaid = percentPrepaid;
    }

    public int getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(int monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }
}
