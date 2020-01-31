package com.tinhvan.hd.customer.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "REGISTER_BY_PHONE")
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "REGISTER_BY_PHONE_SEQ", allocationSize = 1)
public class RegisterByPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "PHONE")
    private String phone;

    @Basic
    @Column(name = "device_id")
    private String deviceId;

    @Basic
    @Column(name = "STATUS")
    private Integer status;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
