package com.tinhvan.hd.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONFIG_SEND_MAIL")
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONFIG_SEND_MAIL_SEQ", allocationSize = 1)
public class ConfigSendMail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Long id;


    @Basic
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    // 1: sign up loan ; 2: sign up promotion
    @Basic
    @Column(name = "TYPE")
    private Integer type;


    @Basic
    @Column(name = "PATH_FILE")
    private String pathFile;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }


}
