package com.tinhvan.hd.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ContractInfo {

    private String contractNumber;

    private String firstName;

    private String midName;

    private String lastName;

    private Date birthday;

    //cmnd
    private String nationalID;
    // ngay cap cmnd
    private Date dateRangeNationalID;
    // dia chi so ho khau
    private String addressFamilyBookNo;
    // dia chi khach hang
    private String address;

    private String phoneNumber;

    private String familyBookNo;

    // bang lai xe
    private String driversLicence;

    private String profession;

    // thu nhap hang thang
    private BigDecimal monthlyNetSalary;

    private String posName;

    private String posAddress;

    // khoan cap von
    private BigDecimal loanAmount;

    // ky han (bao nhieu thang)
    private BigDecimal tenor;

    //lai suat thuc te hang thang
    private BigDecimal interestRate;

    // khoang thanh toan hang thang
    private BigDecimal monthlyInstallmentAmount;

    // ngay thanh toan hang thang
    private BigDecimal monthlyDueDate;

    // ngay thanh toan dau tien
    private Date firstDue;

    // ngay thanh toan cuoi cung
    private Date endDue;

    private String productName;

    private BigDecimal productPrice;

    private String productNameBHVC;

    private BigDecimal productPriceBHVC;

    // so khung
    private String chassisNo;

    // so may
    private String enginerNo;

    // so serial
    private String serialNo;

    // khoan tra truoc
    private String prePayment;

    private String accountName;

    private String bankName;

    private String accountNo;

    private BigDecimal bankFee;

    // loai bao hiem
    private String liType;

    // phi bao hiem
    private BigDecimal lyMonthly;

    private String status;

    private char isInsurance;

    private String loanName;

    private Integer tenorRemaind;

    private Integer totalPaid;

    private List<ConfigCheckRecords> configRecords;

    private String loanType;

    private UUID contractUuid;

    // ngay bat dau trang thai
    private Date documentVerificationDate;

    //
    private String schemeCode;

    private String email;

    // so tien hoan tra hang thang
    private BigDecimal refunDamTP;

    // so tien thanh toan sau khi tru so tien hoan tra
    private BigDecimal amountAfterRefund;

    // ma SI
    private String caCode;

    // ten SI
    private String caName;

    // email SI,chua bao gom @hdsaison.com.vn
    private String caAd;

    // ten cong ty bao hiem
    private String issuerName;

    // lai suat theo du no giam dan
    private BigDecimal effectiveMroi;

    // lai suat theo du no ban dau
    private BigDecimal emiPercent;

    private List<String> attachments;

    private List<String> lstAdj;

    public List<String> getLstAdj() {
        return lstAdj;
    }

    public void setLstAdj(List<String> lstAdj) {
        this.lstAdj = lstAdj;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public Date getDateRangeNationalID() {
        return dateRangeNationalID;
    }

    public void setDateRangeNationalID(Date dateRangeNationalID) {
        this.dateRangeNationalID = dateRangeNationalID;
    }

    public String getAddressFamilyBookNo() {
        return addressFamilyBookNo;
    }

    public void setAddressFamilyBookNo(String addressFamilyBookNo) {
        this.addressFamilyBookNo = addressFamilyBookNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public BigDecimal getMonthlyNetSalary() {
        return monthlyNetSalary;
    }

    public void setMonthlyNetSalary(BigDecimal monthlyNetSalary) {
        this.monthlyNetSalary = monthlyNetSalary;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getPosAddress() {
        return posAddress;
    }

    public void setPosAddress(String posAddress) {
        this.posAddress = posAddress;
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductNameBHVC() {
        return productNameBHVC;
    }

    public void setProductNameBHVC(String productNameBHVC) {
        this.productNameBHVC = productNameBHVC;
    }

    public BigDecimal getProductPriceBHVC() {
        return productPriceBHVC;
    }

    public void setProductPriceBHVC(BigDecimal productPriceBHVC) {
        this.productPriceBHVC = productPriceBHVC;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEnginerNo() {
        return enginerNo;
    }

    public void setEnginerNo(String enginerNo) {
        this.enginerNo = enginerNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(String prePayment) {
        this.prePayment = prePayment;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getBankFee() {
        return bankFee;
    }

    public void setBankFee(BigDecimal bankFee) {
        this.bankFee = bankFee;
    }

    public String getLiType() {
        return liType;
    }

    public void setLiType(String liType) {
        this.liType = liType;
    }

    public BigDecimal getLyMonthly() {
        return lyMonthly;
    }

    public void setLyMonthly(BigDecimal lyMonthly) {
        this.lyMonthly = lyMonthly;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ConfigCheckRecords> getConfigRecords() {
        return configRecords;
    }

    public void setConfigRecords(List<ConfigCheckRecords> configRecords) {
        this.configRecords = configRecords;
    }



    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public char getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(char isInsurance) {
        this.isInsurance = isInsurance;
    }

    public Integer getTenorRemaind() {
        return tenorRemaind;
    }

    public void setTenorRemaind(Integer tenorRemaind) {
        this.tenorRemaind = tenorRemaind;
    }

    public Integer getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Integer totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }


    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getRefunDamTP() {
        return refunDamTP;
    }

    public void setRefunDamTP(BigDecimal refunDamTP) {
        this.refunDamTP = refunDamTP;
    }

    public BigDecimal getAmountAfterRefund() {
        return amountAfterRefund;
    }

    public void setAmountAfterRefund(BigDecimal amountAfterRefund) {
        this.amountAfterRefund = amountAfterRefund;
    }

    public String getCaCode() {
        return caCode;
    }

    public void setCaCode(String caCode) {
        this.caCode = caCode;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public String getCaAd() {
        return caAd;
    }

    public void setCaAd(String caAd) {
        this.caAd = caAd;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public BigDecimal getEffectiveMroi() {
        return effectiveMroi;
    }

    public void setEffectiveMroi(BigDecimal effectiveMroi) {
        this.effectiveMroi = effectiveMroi;
    }

    public BigDecimal getEmiPercent() {
        return emiPercent;
    }

    public void setEmiPercent(BigDecimal emiPercent) {
        this.emiPercent = emiPercent;
    }

    public Date getDocumentVerificationDate() {
        return documentVerificationDate;
    }

    public void setDocumentVerificationDate(Date documentVerificationDate) {
        this.documentVerificationDate = documentVerificationDate;
    }
}
