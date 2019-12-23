package com.tinhvan.hd.staff.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "staff_log")
public class StaffLog {
    @Id
    @SequenceGenerator(name = "staff_log_sequence", sequenceName = "staff_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_log_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "uuid_staff", columnDefinition = "VARCHAR(36)")
    private UUID uuidStaff;

    @Column(name = "email", columnDefinition = "VARCHAR(128)")
    private String email;

    @Column(name = "full_name", columnDefinition = "VARCHAR(128)")
    private String fullName;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "department", columnDefinition = "VARCHAR(128)")
    private String department;

    @Column(name = "area", columnDefinition = "VARCHAR(128)")
    private String area;

    @Column(name = "status", columnDefinition = "VARCHAR(1)")
    private int status;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    private UUID createdBy;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Column(name = "modified_by", columnDefinition = "VARCHAR(36)")
    private UUID modifiedBy;

    @Column(name = "staff_token", columnDefinition = "VARCHAR(300)")
    private String staffToken;

    @Column(name = "created_at_log")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAtLog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuidStaff() {
        return uuidStaff;
    }

    public void setUuidStaff(UUID uuidStaff) {
        this.uuidStaff = uuidStaff;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
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

    public String getStaffToken() {
        return staffToken;
    }

    public void setStaffToken(String staffToken) {
        this.staffToken = staffToken;
    }

    public Date getCreatedAtLog() {
        return createdAtLog;
    }

    public void setCreatedAtLog(Date createdAtLog) {
        this.createdAtLog = createdAtLog;
    }
}
