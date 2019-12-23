package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="customer_token")
public class CustomerToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="customer_token_sequence",sequenceName="customer_token_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="customer_token_sequence")
    private int id;

    @Column(name = "customer_uuid", nullable = false)
    private UUID customerUuid;  //customer uuid

    @Column(name = "token", nullable = false, length = 300)
    private String token;   //JWT

    @Column(name = "device_uuid")
    private UUID deviceUuid;

    @Column(name = "device")
    private String device;

    @Column(name = "os", length = 30)
    private String os;

    @Column(name = "created_at")
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date createAt;

    @Column(name = "deleted_at")
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date deletedAt;

    @Column(name = "status", columnDefinition="SMALLINT")
    private int status;   //0: disable, 1: enable

    @Column(name = "environment")
    private String environment;

    public CustomerToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(UUID deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "CustomerToken{" +
                "id=" + id +
                ", customerUuid=" + customerUuid +
                ", token='" + token + '\'' +
                ", deviceUuid=" + deviceUuid +
                ", device='" + device + '\'' +
                ", os='" + os + '\'' +
                ", createAt=" + createAt +
                ", deletedAt=" + deletedAt +
                ", status=" + status +
                ", environment='" + environment + '\'' +
                '}';
    }
}
