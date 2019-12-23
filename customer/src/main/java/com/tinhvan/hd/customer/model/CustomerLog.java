package com.tinhvan.hd.customer.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_log")
public class CustomerLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="customer_log_sequence",sequenceName="customer_log_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="customer_log_sequence")
    private int id;

    @Column(name = "uuid", nullable=false)
    private UUID uuid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", length = 50)
    private String email;   //check format

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;   //check format

    @Column(name = "status", columnDefinition="SMALLINT")
    private int status;   //0: disable, 1: enable

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "is_delete", columnDefinition="SMALLINT")
    private int isDelete;   //0: disable, 1: enable

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

    @Column(name = "prefer_language", columnDefinition="CHAR(2)", length = 2)
    private String preferLanguage; //vi, en, cn, ...

    @Column(name = "object_version")
    private int objectVersion;    //increase when update

    @Column(name = "require_change_password", columnDefinition="SMALLINT")
    private int requireChangePassword;   //0: disable, 1: enable

    @Column(name = "last_modify_password")
    private Date lastModifyPassword;

    public CustomerLog() {
        super();
    }

    public CustomerLog(Customer customer) {
        this.uuid = customer.getUuid();
        this.fullName = customer.getFullName();
        this.username = customer.getUsername();
        this.password = customer.getPassword();
        this.permanentAddress = customer.getPermanentAddress();
        this.lastModifyPassword = customer.getLastModifyPassword();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.createdAt = customer.getCreatedAt();
        this.createdBy = customer.getCreatedBy();
        this.modifiedAt = customer.getModifiedAt();
        this.modifiedBy = customer.getModifiedBy();
        this.deletedAt = customer.getDeletedAt();
        this.deletedBy = customer.getDeletedBy();
        this.objectVersion = customer.getObjectVersion();
        this.status = customer.getStatus();
        this.isDelete = customer.getIsDelete();
        this.preferLanguage = customer.getPreferLanguage();
        this.requireChangePassword = customer.getRequireChangePassword();
    }

    @Override
    public String toString() {
        return "CustomerLog{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isDelete=" + isDelete +
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
                '}';
    }
}
