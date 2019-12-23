package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_ESIGNED_FILE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_ESIGNED_FILE_SEQ", allocationSize = 1)
public class ContractEsignedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractId;

    @Basic
    @Column(name = "contract_esigned_id")
    private Integer contractEsignedId;

    @Basic
    @Column(name = "contract_file_id")
    private Integer contractFileId;

    @Basic
    @Column(name = "CONTRACT_ESIGNED_FILE")
    private String contractFileName;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "FILE_NAME",length = 300)
    private String fileName;

    @Basic
    @Column(name = "contract_file_type")
    private String contractFileType;

    @Column(name="idx")
    private int idx;

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public String getContractFileName() {
        return contractFileName;
    }

    public void setContractFileName(String contractFileName) {
        this.contractFileName = contractFileName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractEsignedId() {
        return contractEsignedId;
    }

    public void setContractEsignedId(Integer contractEsignedId) {
        this.contractEsignedId = contractEsignedId;
    }

    public Integer getContractFileId() {
        return contractFileId;
    }

    public void setContractFileId(Integer contractFileId) {
        this.contractFileId = contractFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getContractFileType() {
        return contractFileType;
    }

    public void setContractFileType(String contractFileType) {
        this.contractFileType = contractFileType;
    }
}
