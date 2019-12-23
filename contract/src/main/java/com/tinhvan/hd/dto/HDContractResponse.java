package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;

public class HDContractResponse {

   // ma hop dong
    private String contractNumber;

    // ngay bat dau trang thai
    private String documentVerificationDate;

    // ngay ky hop dong
    private String contractPrintingDate;

    private String firstName;

    private String midName;

    private String lastName;

    // so dien thoai cua khach hang
    private String phoneNumber;

    // ngay cap nhat sdt
    private Date lastUpdateApplicant;

    // ngay thang nam sinh
    private Date dob;

    // chung minh nhan dan
    private String nationalID;

    // so ho khau
    private String familyBookNo;

    // bang lai xe
    private String driversLicence;

    // ngay
    private String contractPrintingCompleted;

    // khoan cap von
    private BigDecimal loanAmount;

    // ky han (bao nhieu thang)
    private BigDecimal tenor;

    // khoang thanh toan hang thang
    private BigDecimal monthlyInstallmentAmount;

    // ngay thanh toan hang thang
    private BigDecimal monthlyDueDate;

    // ngay thanh toan dau tien
    private Date firstDue;

    // ngay thanh toan cuoi cung
    private Date endDue;

    // bao hiem khoan vay (Y or N)
    private char isInsurance;

    // trang thai vay (Live,...)
    private String status;

    private Boolean isPhoneRequest;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getLastUpdateApplicant() {
        return lastUpdateApplicant;
    }

    public void setLastUpdateApplicant(Date lastUpdateApplicant) {
        this.lastUpdateApplicant = lastUpdateApplicant;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getFamilyBookNo() {
        return familyBookNo;
    }

    public void setFamilyBookNo(String familyBookNo) {
        this.familyBookNo = familyBookNo;
    }

    public String getDriversLicence() {
        return driversLicence;
    }

    public void setDriversLicence(String driversLicence) {
        this.driversLicence = driversLicence;
    }

    public String getContractPrintingCompleted() {
        return contractPrintingCompleted;
    }

    public void setContractPrintingCompleted(String contractPrintingCompleted) {
        this.contractPrintingCompleted = contractPrintingCompleted;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
    }

    public BigDecimal getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(BigDecimal monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public BigDecimal getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(BigDecimal monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public Date getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(Date firstDue) {
        this.firstDue = firstDue;
    }

    public Date getEndDue() {
        return endDue;
    }

    public void setEndDue(Date endDue) {
        this.endDue = endDue;
    }

    public char getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(char isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPhoneRequest() {
        return isPhoneRequest;
    }

    public void setPhoneRequest(Boolean phoneRequest) {
        isPhoneRequest = phoneRequest;
    }
}
