package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "CONTRACT_ESIGNED_ID")
    private Integer contractEsignedId;


    @Basic
    @Column(name = "CONTRACT_FILE_ID")
    private Integer contractFileId;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

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


}
