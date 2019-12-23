package com.tinhvan.hd.base.enities;

import java.util.Date;
import java.util.UUID;

/**
 * @author tuongnk on 7/2/2019
 * @project notification
 */
public class CustomerVO {

    private UUID uuid;

    private String firstName;

    private String middleName;

    private String lastName;

    private String username;

    private String password;

    private String identityCardNumber;

    private short gender;   //1: male, 2:female

    private Date birthdate;

    private String email;   //check format

    private String phoneNumber;   //check format

    private String[] alternativePhoneNumbers;

    private String address;

    private String ward;    //reference json data

    private String district;    //reference json data

    private String city;    //reference json data

    private String nationality;

    private Date createdAt;

    private Date modifiedAt;

    private UUID modifiedBy;

    private short maritalStatus;

    private String occupation;

    private UUID deletedBy;

    private int objectVersion;    //increase when update

    private int status;   //0: disable, 1: enable

    private String preferLanguage; //vi, en, cn, ...

    private int requireChangePassword;   //0: disable, 1: enable

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public short getGender() {
        return gender;
    }

    public void setGender(short gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getAlternativePhoneNumbers() {
        return alternativePhoneNumbers;
    }

    public void setAlternativePhoneNumbers(String[] alternativePhoneNumbers) {
        this.alternativePhoneNumbers = alternativePhoneNumbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public short getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(short maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public int getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public int getRequireChangePassword() {
        return requireChangePassword;
    }

    public void setRequireChangePassword(int requireChangePassword) {
        this.requireChangePassword = requireChangePassword;
    }
}
