package com.tinhvan.hd.filehandler.payload;

public class AdjustmentFrom implements BasePayload {
    public String currentDate;
    public String fullName;
    public String birthday;
    public String nationalID;
    public String addressFamilyBookNo;
    public String address;
    public String phoneNumber;
    public String email;
    public String profession;
    public String monthlyNetSalary;
    public String suggestDate;
    public String creditNo;
    public String creditDate;
    public String mortgageNo;
    public String mortgageDate;
    public String insuranceDate;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMonthlyNetSalary() {
        return monthlyNetSalary;
    }

    public void setMonthlyNetSalary(String monthlyNetSalary) {
        this.monthlyNetSalary = monthlyNetSalary;
    }

    public String getSuggestDate() {
        return suggestDate;
    }

    public void setSuggestDate(String suggestDate) {
        this.suggestDate = suggestDate;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }

    public String getMortgageNo() {
        return mortgageNo;
    }

    public void setMortgageNo(String mortgageNo) {
        this.mortgageNo = mortgageNo;
    }

    public String getMortgageDate() {
        return mortgageDate;
    }

    public void setMortgageDate(String mortgageDate) {
        this.mortgageDate = mortgageDate;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    @Override
    public void validatePayload() {
        if (currentDate == null)
            currentDate = "";
        if (fullName == null)
            fullName = "";
        if (birthday == null)
            birthday = "";
        if (nationalID == null)
            nationalID = "";
        if (addressFamilyBookNo == null)
            addressFamilyBookNo = "";
        if (address == null)
            address = "";
        if (phoneNumber == null)
            phoneNumber = "";
        if (email == null)
            email = "";
        if (profession == null)
            profession = "";
        if (monthlyNetSalary == null)
            monthlyNetSalary = "";
        if (suggestDate == null)
            suggestDate = "";
        if (creditNo == null)
            creditNo = "";
        if (creditDate == null)
            creditDate = "";
        if (mortgageNo == null)
            mortgageNo = "";
        if (mortgageDate == null)
            mortgageDate = "";
        if (insuranceDate == null)
            insuranceDate = "";
    }
}
