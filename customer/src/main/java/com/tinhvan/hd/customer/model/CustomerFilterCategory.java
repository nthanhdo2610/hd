package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_filter_category")
public class CustomerFilterCategory implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "customer_filter_category_sequence", sequenceName = "customer_filter_category_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_filter_category_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column
    private String key;

    @Column
    private String label;

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
        if(HDUtil.isNullOrEmpty(key))
            throw new BadRequestException(1113, "customer filter key is empty");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        return "CustomerFilterCategory{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", label='" + label + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}
