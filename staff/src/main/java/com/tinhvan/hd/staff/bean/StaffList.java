package com.tinhvan.hd.staff.bean;

import java.util.UUID;

public class StaffList {
    private UUID id;
    private String email;
    private String fullName;
    private String department;
    private String area;
    private String roleCode;
    private String roleName;
    private String staffToken;
    private int objectVersion = 0;
    private int status = 1;

    public StaffList(UUID id, String email, String fullName, String department, String area, String roleCode, String roleName, int objectVersion, int status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.department = department;
        this.area = area;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.objectVersion = objectVersion;
        this.status = status;
    }

    public StaffList() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
