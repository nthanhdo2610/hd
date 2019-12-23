package com.tinhvan.hd.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_log_action")
public class CustomerLogAction {
    @Id
    @SequenceGenerator(name = "customer_log_action_sequence", sequenceName = "customer_log_action_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_log_action_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "contract_code", columnDefinition = "VARCHAR(100)")
    private String contractCode;

    @Column(name = "action", columnDefinition = "VARCHAR(1024)")
    private String action;

    @Column(name = "para", columnDefinition = "VARCHAR(500)")
    private String para;

    @Column(name = "object_name", columnDefinition = "VARCHAR(255)")
    private String objectName;

    @Column(name = "value_old", columnDefinition = "VARCHAR(4000)")
    private String valueOld;

    @Column(name = "value_new", columnDefinition = "VARCHAR(4000)")
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

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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
