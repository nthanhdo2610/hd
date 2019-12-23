package com.tinhvan.hd.model;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.bean.ConfigAdjustmentContractUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "config_adjustment_contract")
public class ConfigAdjustmentContract implements HDPayload {
    @Id
    @SequenceGenerator(name = "config_adjustment_contract_sequence", sequenceName = "config_adjustment_contract_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_adjustment_contract_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(50)")
    private String code;

    @Column(name = "name", columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(name = "is_check_document")
    private int isCheckDocument;

    @Column(name = "is_adjustment_document")
    private int isAdjustmentDocument;

    @Column(name = "idx")
    private Integer idx;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private Integer status = 1;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    private UUID createdBy;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Column(name = "modified_by", columnDefinition = "VARCHAR(36)")
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsCheckDocument() {
        return isCheckDocument;
    }

    public void setIsCheckDocument(int isCheckDocument) {
        this.isCheckDocument = isCheckDocument;
    }

    public int getIsAdjustmentDocument() {
        return isAdjustmentDocument;
    }

    public void setIsAdjustmentDocument(int isAdjustmentDocument) {
        this.isAdjustmentDocument = isAdjustmentDocument;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
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
        if (id <= 0)
            throw new BadRequestException(1106, "invalid id");
        if (isCheckDocument < 0 && isCheckDocument > 1)
            throw new BadRequestException(1223, "invalid is check document");
        if (isAdjustmentDocument < 0 && isAdjustmentDocument > 1)
            throw new BadRequestException(1224, "invalid is adjustment document");
    }

    @Override
    public String toString() {
        return "ConfigAdjustmentContract{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", isCheckDocument=" + isCheckDocument +
                ", isAdjustmentDocument=" + isAdjustmentDocument +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", modifiedAt=" + modifiedAt +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}
