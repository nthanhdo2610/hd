package com.tinhvan.hd.base.enities;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Table(name="ROLE")
@SequenceGenerator(name="ROLE_SEQUENCE", sequenceName="role_seq", allocationSize=1)
public class RoleEntity implements Serializable, HDPayload {

    private static final long serialVersionUID = -8108213279230972124L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQUENCE")
    @Column(name="id", updatable=false, nullable=false)
    @Type(type="org.hibernate.type.LongType")
    private Long id;

    @Column(name="role")
    @Type(type="org.hibernate.type.StringType")
    private String role;

    @Column(name="name")
    @Type(type = "org.hibernate.type.StringType")
    private String name;

    @Column(name="status")
    @Type(type="org.hibernate.type.IntegerType")
    private Integer status;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Basic
    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public void validatePayload() {

    }
}

