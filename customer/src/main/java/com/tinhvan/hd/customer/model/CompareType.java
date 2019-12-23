package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "compare_type")
public class CompareType implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "compare_type_sequence", sequenceName = "compare_type_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compare_type_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column
    private String name;

    @Column
    private String code;

    @Column(columnDefinition = "SMALLINT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int active;

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

    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(code))
            throw new BadRequestException(1114, "compare type code is empty");
        if(HDUtil.isNullOrEmpty(name))
            this.name = this.code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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
        return "CompareType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}
