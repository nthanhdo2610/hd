package com.tinhvan.hd.filehandler.payload;

import com.tinhvan.hd.filehandler.utils.BaseUtil;
import software.amazon.ion.Decimal;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContractForm {
    public String emptyValue = "";
    public String contractNumber = emptyValue;
    public String signDate = emptyValue;
    public String fullName = emptyValue;
    public String birthday = emptyValue;
    public String nationalID = emptyValue;
    public String addressFamilyBookNo = emptyValue;
    public String address = emptyValue;
    public String phoneNumber = emptyValue;
    public String profession = emptyValue;
    public String position = emptyValue;
    public String email = emptyValue;
    public String monthlyNetSalary = emptyValue;

    public String productName = emptyValue;
    public String brandName = emptyValue;
    public String engineNo = emptyValue;
    public String chassisNo = emptyValue;
    public String productPrice = emptyValue;
    public String prePayment = emptyValue;
    public String loanAmount = emptyValue;
    public String tenor = emptyValue;
    public String monthlyInstallmentAmount = emptyValue;
    public String interestRate = emptyValue;
    public String firstDue = emptyValue;
    public String monthlyDueDate = emptyValue;
    public String endDue = emptyValue;

    public String bankName = emptyValue;
    public String accountName = emptyValue;
    public String accountNo = emptyValue;
    public String bankFee = emptyValue;

    public String productNameBHVC = emptyValue;
    public String productPriceBHVC = emptyValue;
    public String liType = emptyValue;
    public String lyMonthly = emptyValue;
    public String totalPaid = emptyValue;

    public String sCode = emptyValue;
    public String interestRateOutstanding = emptyValue;
    public String dealerName = emptyValue;
    public String dealerAddress = emptyValue;
    public String refundDamTP = emptyValue;
    public String amountAfterRefund = emptyValue;
    public String caCode= emptyValue;
    public String caName= emptyValue;
    public String caAd= emptyValue;
    public String issuerName= emptyValue;
    public String effectiveMroi= emptyValue;
    public List<String> attachments = new ArrayList<>();


    public ContractForm(ContractInfo info) {
        if (info != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Locale locale = new Locale("vn", "VN");
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
            if (info.getContractNumber() != null)
                this.contractNumber = info.getContractNumber();
            this.signDate = df.format(new Date());
            this.fullName = generateFullName(info);
            if (info.getBirthday() != null)
                this.birthday = df.format(info.getBirthday());
            if (info.getNationalID() != null)
                this.nationalID = info.getNationalID();
            if (info.getAddressFamilyBookNo() != null)
                this.addressFamilyBookNo = info.getAddressFamilyBookNo();
            if (info.getAddress() != null)
                this.address = info.getAddress();
            if (info.getPhoneNumber() != null)
                this.phoneNumber = info.getPhoneNumber();
            if (info.getProfession() != null)
                this.profession = info.getProfession();
            if (info.getMonthlyNetSalary() != null)
                this.monthlyNetSalary = numberFormatter.format(info.getMonthlyNetSalary());
            if (info.getProductName() != null) {
                this.productName = info.getProductName();
                this.brandName = info.getProductName();
            }
            if (info.getEnginerNo() != null)
                this.engineNo = info.getEnginerNo();
            if (info.getChassisNo() != null)
                this.chassisNo = info.getChassisNo();
            if (info.getProductPrice() != null)
                this.productPrice = numberFormatter.format(info.getProductPrice());
            if (info.getPrePayment() != null)
                this.prePayment = numberFormatter.format(Decimal.valueOf(info.getPrePayment()));
            if (info.getLoanAmount() != null)
                this.loanAmount = numberFormatter.format(info.getLoanAmount());
            if (info.getTenor() != null)
                this.tenor = numberFormatter.format(info.getTenor());
            if (info.getMonthlyInstallmentAmount() != null)
                this.monthlyInstallmentAmount = numberFormatter.format(info.getMonthlyInstallmentAmount());
            if (info.getInterestRate() != null)
                this.interestRate = numberFormatter.format(info.getInterestRate());
            if (info.getFirstDue() != null)
                this.firstDue = df.format(info.getFirstDue());
            if (info.getMonthlyDueDate() != null)
                this.monthlyDueDate = numberFormatter.format(info.getMonthlyDueDate());
            if (info.getEndDue() != null)
                this.endDue = df.format(info.getEndDue());
            if (info.getBankName() != null)
                this.bankName = info.getBankName();
            if (info.getAccountName() != null)
                this.accountName = info.getAccountName();
            if (info.getAccountNo() != null)
                this.accountNo = info.getAccountNo();
            if (info.getBankFee() != null)
                this.bankFee = numberFormatter.format(info.getBankFee());
            if (info.getProductNameBHVC() != null)
                this.productNameBHVC = info.getProductNameBHVC();
            if (info.getProductPriceBHVC() != null)
                this.productPriceBHVC = numberFormatter.format(info.getProductPriceBHVC());
            if (info.getLiType() != null)
                this.liType = info.getLiType();
            if (info.getLyMonthly() != null)
                this.lyMonthly = numberFormatter.format(info.getLyMonthly());
            if (info.getTotalPaid() != null)
                this.totalPaid = numberFormatter.format(info.getTotalPaid());
            if (info.getSchemeCode() != null)
                this.sCode = info.getSchemeCode();
            if (info.getPosName() != null)
                this.dealerName = info.getPosName();
            if (info.getPosAddress() != null)
                this.dealerAddress = info.getPosAddress();
            if (info.getEmail() != null)
                this.email = info.getEmail();
            if (info.getRefunDamTP() != null)
                this.refundDamTP = numberFormatter.format(info.getRefunDamTP());
            if (info.getAmountAfterRefund() != null)
                this.amountAfterRefund = numberFormatter.format(info.getAmountAfterRefund());
            if (info.getCaCode() != null)
                this.caCode = info.getCaCode();
            if (info.getCaName() != null)
                this.caName = info.getCaName();
            if (info.getCaAd() != null)
                this.caAd = info.getCaAd()+"@hdsaison.com.vn";
            if (info.getIssuerName() != null)
                this.issuerName = info.getIssuerName();
            if (info.getEffectiveMroi() != null)
                this.effectiveMroi = numberFormatter.format(info.getEffectiveMroi());
            if (info.getEmiPercent() != null)
                this.interestRateOutstanding = numberFormatter.format(info.getEmiPercent());
            if (info.getAttachments() != null)
                this.attachments = info.getAttachments();

        }
    }

    String generateFullName(ContractInfo info) {
        String str = "";
        if (!BaseUtil.isNullOrEmpty(info.getLastName()))
            str += info.getLastName() + " ";
        if (!BaseUtil.isNullOrEmpty(info.getMidName()))
            str += info.getMidName() + " ";
        if (!BaseUtil.isNullOrEmpty(info.getFirstName()))
            str += info.getFirstName();
        return str.trim();
    }

    @Override
    public String toString() {
        return "ContractForm{" +
                "emptyValue='" + emptyValue + '\'' +
                ", contractNumber='" + contractNumber + '\'' +
                ", signDate='" + signDate + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nationalID='" + nationalID + '\'' +
                ", addressFamilyBookNo='" + addressFamilyBookNo + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profession='" + profession + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                ", monthlyNetSalary='" + monthlyNetSalary + '\'' +
                ", productName='" + productName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", engineNo='" + engineNo + '\'' +
                ", chassisNo='" + chassisNo + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", prePayment='" + prePayment + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", tenor='" + tenor + '\'' +
                ", monthlyInstallmentAmount='" + monthlyInstallmentAmount + '\'' +
                ", interestRate='" + interestRate + '\'' +
                ", firstDue='" + firstDue + '\'' +
                ", monthlyDueDate='" + monthlyDueDate + '\'' +
                ", endDue='" + endDue + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", bankFee='" + bankFee + '\'' +
                ", productNameBHVC='" + productNameBHVC + '\'' +
                ", productPriceBHVC='" + productPriceBHVC + '\'' +
                ", liType='" + liType + '\'' +
                ", lyMonthly='" + lyMonthly + '\'' +
                ", totalPaid='" + totalPaid + '\'' +
                ", sCode='" + sCode + '\'' +
                ", interestRateOutstanding='" + interestRateOutstanding + '\'' +
                ", dealerName='" + dealerName + '\'' +
                ", dealerAddress='" + dealerAddress + '\'' +
                ", refundDamTP='" + refundDamTP + '\'' +
                ", amountAfterRefund='" + amountAfterRefund + '\'' +
                ", caCode='" + caCode + '\'' +
                ", caName='" + caName + '\'' +
                ", caAd='" + caAd + '\'' +
                ", issuerName='" + issuerName + '\'' +
                ", effectiveMroi='" + effectiveMroi + '\'' +
                ", attachments=" + attachments +
                '}';
    }

    public String getEmptyValue() {
        return emptyValue;
    }

    public void setEmptyValue(String emptyValue) {
        this.emptyValue = emptyValue;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMonthlyNetSalary() {
        return monthlyNetSalary;
    }

    public void setMonthlyNetSalary(String monthlyNetSalary) {
        this.monthlyNetSalary = monthlyNetSalary;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(String prePayment) {
        this.prePayment = prePayment;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public String getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(String monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(String firstDue) {
        this.firstDue = firstDue;
    }

    public String getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(String monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public String getEndDue() {
        return endDue;
    }

    public void setEndDue(String endDue) {
        this.endDue = endDue;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankFee() {
        return bankFee;
    }

    public void setBankFee(String bankFee) {
        this.bankFee = bankFee;
    }

    public String getProductNameBHVC() {
        return productNameBHVC;
    }

    public void setProductNameBHVC(String productNameBHVC) {
        this.productNameBHVC = productNameBHVC;
    }

    public String getProductPriceBHVC() {
        return productPriceBHVC;
    }

    public void setProductPriceBHVC(String productPriceBHVC) {
        this.productPriceBHVC = productPriceBHVC;
    }

    public String getLiType() {
        return liType;
    }

    public void setLiType(String liType) {
        this.liType = liType;
    }

    public String getLyMonthly() {
        return lyMonthly;
    }

    public void setLyMonthly(String lyMonthly) {
        this.lyMonthly = lyMonthly;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getInterestRateOutstanding() {
        return interestRateOutstanding;
    }

    public void setInterestRateOutstanding(String interestRateOutstanding) {
        this.interestRateOutstanding = interestRateOutstanding;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getRefundDamTP() {
        return refundDamTP;
    }

    public void setRefundDamTP(String refundDamTP) {
        this.refundDamTP = refundDamTP;
    }

    public String getAmountAfterRefund() {
        return amountAfterRefund;
    }

    public void setAmountAfterRefund(String amountAfterRefund) {
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

    public String getEffectiveMroi() {
        return effectiveMroi;
    }

    public void setEffectiveMroi(String effectiveMroi) {
        this.effectiveMroi = effectiveMroi;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
