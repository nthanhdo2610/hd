package com.tinhvan.hd.model;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "role_authorize")
public class RoleAuthorize implements HDPayload {

    @Id
    @SequenceGenerator(name = "role_authorize_sequence", sequenceName = "role_authorize_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_authorize_sequence")
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "role_id")
    private int roleId;
    @Basic
    @Column(name = "services_id")
    private int servicesId;
    @Basic
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic
    @Column(name = "created_by")
    private UUID createBy;
    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getServicesId() {
        return servicesId;
    }

    public void setServicesId(int servicesId) {
        this.servicesId = servicesId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreateBy() {
        return createBy;
    }

    public void setCreateBy(UUID createBy) {
        this.createBy = createBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void validatePayload() {

    }
}
