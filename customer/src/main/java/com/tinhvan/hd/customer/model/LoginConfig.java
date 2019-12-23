package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_login_config")
public class LoginConfig implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "customer_login_config_sequence", sequenceName = "customer_login_config_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_login_config_sequence")
    private int id;

    @Column(name = "fail_count_time")
    private int countTime;

    @Column(name = "locked_time")
    private int lockedTime;

    @Column(name = "created_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt;

    @Column(name = "modified_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date modifiedAt;

    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID createdBy;

    @Column(name = "modified_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountTime() {
        return countTime;
    }

    public void setCountTime(int countTime) {
        this.countTime = countTime;
    }

    public int getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(int lockedTime) {
        this.lockedTime = lockedTime;
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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "LoginConfig{" +
                "id=" + id +
                ", countTime=" + countTime +
                ", lockedTime=" + lockedTime +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}
