package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.customer.payload.ContractResponse;
import com.tinhvan.hd.customer.payload.UpdateRequest;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "USER_NAME_SHOW" ,length = 128)
    private String userNameShow;

    @Column(name = "email", length = 128)
    private String email;   //check format

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;   //check format

    @Column(name = "phone_number_origin", length = 20)
    private String phoneNumberOrigin;   //check format

    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status;   //0: disable, 1: enable

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "is_delete", columnDefinition = "SMALLINT")
    private int isDelete;   //0: disable, 1: enable

    @Column(name = "register_type", columnDefinition = "SMALLINT")
    private Integer registerType = 1;   //1: register by phone

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "prefer_language", columnDefinition = "CHAR(2)", length = 2)
    private String preferLanguage; //vi, en, cn, ...

    @Column(name = "object_version")
    private int objectVersion;    //increase when update

    @Column(name = "require_change_password", columnDefinition = "SMALLINT")
    private int requireChangePassword;   //0: disable, 1: enable

    @Column(name = "last_modify_password")
    private Date lastModifyPassword;

    @Column(name = "count_login_fail")
    private int countLoginFail;

    @Column(name = "locked_login_at")
    private Date lockedLoginAt;

    @Column(name = "last_login_fail_at")
    private Date lastLoginFailAt;

    @Column(name = "phone_otp_register", length = 20)
    private String phoneOtpRegister;   //check format

    @Column(name = "is_send_otp")
    private int isSendOtp;

    @Column(name = "is_register_password")
    private int isRegisterPassword;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "first_login_at")
    private Date firstLoginAt;

    @Transient
    private Date lockedLoginTo;

    @Transient
    private List<ContractResponse> contacts;

    public Date getLockedLoginTo() {
        return lockedLoginTo;
    }

    public void setLockedLoginTo(Date lockedLoginTo) {
        this.lockedLoginTo = lockedLoginTo;
    }

    public Customer() {
        super();
    }

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

    public List<ContractResponse> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContractResponse> contacts) {
        this.contacts = contacts;
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

    public String getPhoneNumberOrigin() {
        return phoneNumberOrigin;
    }

    public void setPhoneNumberOrigin(String phoneNumberOrigin) {
        this.phoneNumberOrigin = phoneNumberOrigin;
    }

    public void init(Date now, String preferLanguage) {
        this.uuid = UUID.randomUUID();
        //this.lastModifyPassword = now;
        this.objectVersion = 1;
        this.status = -1;
        this.createdBy = this.uuid;
        this.createdAt = now;
        this.preferLanguage = preferLanguage;
        this.requireChangePassword = HDConstant.STATUS.DISABLE;

    }

    public void update(UpdateRequest updateRequest, Date modifiedAt, UUID modifiedBy, String preferLanguage) {
        increaseObjectVersion();
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.preferLanguage = preferLanguage;
        this.fullName = updateRequest.getFullName();
        this.email = updateRequest.getEmail();
        //this.permanentAddress = updateRequest.getPermanentAddress();
    }

    public void changeStatus(Date modifiedAt, UUID modifiedBy, int status) {
        increaseObjectVersion();
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.status = status;
    }

    public void increaseObjectVersion() {
        this.objectVersion = this.objectVersion + 1;
    }



    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fullName='" + fullName + '\'' +
                ", userNameShow='" + userNameShow + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isDelete=" + isDelete +
                ", registerType=" + registerType +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", modifiedBy=" + modifiedBy +
                ", deletedBy=" + deletedBy +
                ", deletedAt=" + deletedAt +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", preferLanguage='" + preferLanguage + '\'' +
                ", objectVersion=" + objectVersion +
                ", requireChangePassword=" + requireChangePassword +
                ", lastModifyPassword=" + lastModifyPassword +
                ", countLoginFail=" + countLoginFail +
                ", lockedLoginAt=" + lockedLoginAt +
                ", lastLoginFailAt=" + lastLoginFailAt +
                ", phoneOtpRegister='" + phoneOtpRegister + '\'' +
                ", isSendOtp=" + isSendOtp +
                ", isRegisterPassword=" + isRegisterPassword +
                ", avatar='" + avatar + '\'' +
                ", firstLoginAt=" + firstLoginAt +
                ", lockedLoginTo=" + lockedLoginTo +
                ", contacts=" + contacts +
                '}';
    }

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public static final class RegisterType {
        public static final int CONTRACT = 1;
        public static final int PHONE = 2;
    }
}