package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_FILE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_FILE_SEQ", allocationSize = 1)
public class ContractFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;


    @Basic
    @Column(name = "FILE_NAME",length = 300)
    private String fileName;

    @Basic
    @Column(name = "ORDERING",columnDefinition = "SMALLINT")
    private Integer ordering;

    @Basic
    @Column(name = "IS_ESIGN",columnDefinition = "SMALLINT")
    private Integer isEsign;

    @Basic
    @Column(name = "FILE_TYPE",columnDefinition = "SMALLINT")
    private Integer fileType;


    @Basic
    @Column(name = "FILE_TAIL",columnDefinition = "char")
    private String fileTail;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public Integer getIsEsign() {
        return isEsign;
    }

    public void setIsEsign(Integer isEsign) {
        this.isEsign = isEsign;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileTail() {
        return fileTail;
    }

    public void setFileTail(String fileTail) {
        this.fileTail = fileTail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
