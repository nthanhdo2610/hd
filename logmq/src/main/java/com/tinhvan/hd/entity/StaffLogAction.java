package com.tinhvan.hd.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "staff_log_action")
public class StaffLogAction {
    @Id
    @SequenceGenerator(name = "staff_log_action_sequence", sequenceName = "staff_log_action_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_log_action_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "staff_id")
    private UUID staffId;

    @Column(name = "action", columnDefinition = "VARCHAR(1024)")
    private String action;

    @Column(name = "para", columnDefinition = "VARCHAR(500)")
    private String para;

    @Column(name = "object_name", columnDefinition = "VARCHAR(255)")
    private String objectName;

    @Column(name = "value_old", columnDefinition = "VARCHAR(1024)")
    private String valueOld;

    @Column(name = "value_new", columnDefinition = "VARCHAR(1024)")
    private String valueNew;

    @Column(name = "device", columnDefinition = "VARCHAR(20)")
    private String device;

    @Column(name = "type", columnDefinition = "VARCHAR(100)")
    private String type;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getValueOld() {
        return valueOld;
    }

    public void setValueOld(String valueOld) {
        this.valueOld = valueOld;
    }

    public String getValueNew() {
        return valueNew;
    }

    public void setValueNew(String valueNew) {
        this.valueNew = valueNew;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

}
