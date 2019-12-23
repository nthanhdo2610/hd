/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author LUUBI
 */
@Entity
@Table(name = "staff")
public class Staff implements HDPayload, Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "email", columnDefinition = "VARCHAR(128)")
    private String email;
    @Column(name = "full_name", columnDefinition = "VARCHAR(128)")
    private String fullName;
    @Column(name = "department", columnDefinition = "VARCHAR(128)")
    private String department;
    @Column(name = "area", columnDefinition = "VARCHAR(128)")
    private String area;
    @Column(name = "role_code", columnDefinition = "VARCHAR(100)")
    private String roleCode;
    @Column(name = "role_name", columnDefinition = "VARCHAR(128)")
    private String roleName;

    @Column(name = "staff_token", columnDefinition = "VARCHAR(300)")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String staffToken;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "modified_at")
    private Date modifiedAt;
    @Column(name = "modified_by")
    private UUID modifiedBy;
    @Column(name = "object_version")
    private int objectVersion = 0;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status = 1;

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStaffToken() {
        return staffToken;
    }

    public void setStaffToken(String staffToken) {
        this.staffToken = staffToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void validatePayload() {
        //validate email
        if (!HDUtil.isNullOrEmpty(email) && !HDUtil.isEmail(email)) {
            throw new BadRequestException(1110, "invalid email");
        }
    }

    public void init() {
        this.id = UUID.randomUUID();
    }

}
