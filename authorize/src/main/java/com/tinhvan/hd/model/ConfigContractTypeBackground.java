package com.tinhvan.hd.model;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
@Entity
@Table(name = "config_contract_type_background")
public class ConfigContractTypeBackground implements HDPayload {
    @Id
    @SequenceGenerator(name = "config_contract_type_background_sequence", sequenceName = "config_contract_type_background_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_contract_type_background_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "contract_type", columnDefinition = "VARCHAR(20)")
    private String contractType;

    @Column(name = "contract_name", columnDefinition = "VARCHAR(128)")
    private String contractName;

    @Column(name = "backgroup_image_link", columnDefinition = "VARCHAR(300)")
    private String backgroupImageLink;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getBackgroupImageLink() {
        return backgroupImageLink;
    }

    public void setBackgroupImageLink(String backgroupImageLink) {
        this.backgroupImageLink = backgroupImageLink;
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

    }

    @Override
    public String toString() {
        return "ConfigContractTypeBackground{" +
                "id=" + id +
                ", contractType='" + contractType + '\'' +
                ", contractName='" + contractName + '\'' +
                ", backgroupImageLink='" + backgroupImageLink + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", modifiedAt=" + modifiedAt +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}
