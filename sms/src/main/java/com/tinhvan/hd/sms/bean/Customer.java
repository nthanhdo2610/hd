package com.tinhvan.hd.sms.bean;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Customer {

    private int id;

    private UUID uuid;

    private String fullName;

    private String userNameShow;

    private String email;   //check format

    private String phoneNumber;   //check format

    private String phoneNumberOrigin;

    private int status;   //0: disable, 1: enable

    private String username;

    private String password;

    private int isDelete;   //0: disable, 1: enable

    private int registerType = 1;   //1: register by phone

    private UUID createdBy;

    private Date createdAt;

    private Date modifiedAt;

    private UUID modifiedBy;

    private UUID deletedBy;

    private Date deletedAt;

    private String permanentAddress;

    private String preferLanguage; //vi, en, cn, ...

    private int objectVersion;    //increase when update

    private int requireChangePassword;   //0: disable, 1: enable

    private Date lastModifyPassword;

    private int countLoginFail;

    private Date lockedLoginAt;

    private Date lastLoginFailAt;

    private String phoneOtpRegister;   //check format

    private int isSendOtp;

    private int isRegisterPassword;

    private String avatar;

    private Date firstLoginAt;

    private Date lockedLoginTo;

    private List<ContractResponse> contacts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPhoneNumberOrigin() {
        return phoneNumberOrigin;
    }

    public void setPhoneNumberOrigin(String phoneNumberOrigin) {
        this.phoneNumberOrigin = phoneNumberOrigin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
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

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public int getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    public int getRequireChangePassword() {
        return requireChangePassword;
    }

    public void setRequireChangePassword(int requireChangePassword) {
        this.requireChangePassword = requireChangePassword;
    }

    public Date getLastModifyPassword() {
        return lastModifyPassword;
    }

    public void setLastModifyPassword(Date lastModifyPassword) {
        this.lastModifyPassword = lastModifyPassword;
    }


    public int getCountLoginFail() {
        return countLoginFail;
    }

    public void setCountLoginFail(int countLoginFail) {
        this.countLoginFail = countLoginFail;
    }

    public Date getLockedLoginAt() {
        return lockedLoginAt;
    }

    public void setLockedLoginAt(Date lockedLoginAt) {
        this.lockedLoginAt = lockedLoginAt;
    }

    public Date getLastLoginFailAt() {
        return lastLoginFailAt;
    }

    public void setLastLoginFailAt(Date lastLoginFailAt) {
        this.lastLoginFailAt = lastLoginFailAt;
    }

    public String getPhoneOtpRegister() {
        return phoneOtpRegister;
    }

    public void setPhoneOtpRegister(String phoneOtpRegister) {
        this.phoneOtpRegister = phoneOtpRegister;
    }

    public int getIsSendOtp() {
        return isSendOtp;
    }

    public void setIsSendOtp(int isSendOtp) {
        this.isSendOtp = isSendOtp;
    }

    public int getIsRegisterPassword() {
        return isRegisterPassword;
    }

    public void setIsRegisterPassword(int isRegisterPassword) {
        this.isRegisterPassword = isRegisterPassword;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNameShow() {
        return userNameShow;
    }

    public void setUserNameShow(String userNameShow) {
        this.userNameShow = userNameShow;
    }

    public Date getFirstLoginAt() {
        return firstLoginAt;
    }

    public void setFirstLoginAt(Date firstLoginAt) {
        this.firstLoginAt = firstLoginAt;
    }

    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
    }

    public void increaseObjectVersion() {
        this.objectVersion = this.objectVersion + 1;
    }

    public Date getLockedLoginTo() {
        return lockedLoginTo;
    }

    public void setLockedLoginTo(Date lockedLoginTo) {
        this.lockedLoginTo = lockedLoginTo;
    }

    public List<ContractResponse> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContractResponse> contacts) {
        this.contacts = contacts;
    }
}