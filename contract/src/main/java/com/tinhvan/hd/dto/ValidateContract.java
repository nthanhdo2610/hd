package com.tinhvan.hd.dto;

import java.util.Date;

public class ValidateContract {

    private String firstName;

    private String lastName;

    private String identifyId;

    private String phone;

    private Date birthday;

    private String familyBookNo;

    private String driversLicence;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }
}
