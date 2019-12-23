package com.tinhvan.hd.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_device")
public class CustomerDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "customer_device_sequence", sequenceName = "customer_device_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_device_sequence")
    private Integer id;

    @Column(name = "customer_uuid")
    private UUID customerUuid;

    @Column(name = "fcm_token", length = 300)
    private String fcmToken;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "prefer_language", length = 2)
    private String preferLanguage;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomerDevice{" +
                "id=" + id +
                ", customerUuid=" + customerUuid +
                ", fcmToken='" + fcmToken + '\'' +
                ", createAt=" + createAt +
                ", preferLanguage='" + preferLanguage + '\'' +
                ", status=" + status +
                '}';
    }
}
