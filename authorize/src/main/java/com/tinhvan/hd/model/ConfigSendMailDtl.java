package com.tinhvan.hd.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONFIG_SEND_MAIL_DTL")
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONFIG_SEND_MAIL_DTL_SEQ", allocationSize = 1)
public class ConfigSendMailDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Long id;

    @Basic
    @Column(name = "CONFIG_SEND_MAIL_ID")
    private Long configSendMailId;

    @Basic
    @Column(name = "MAIL_lIST")
    private String mailList;

    @Basic
    @Column(name = "PROVINCE")
    private String province;

    @Basic
    @Column(name = "DISTRICT")
    private String district;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public ConfigSendMailDtl() {
        super();
    }

    public ConfigSendMailDtl(String province, String district) {
        this.province = province;
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigSendMailId() {
        return configSendMailId;
    }

    public void setConfigSendMailId(Long configSendMailId) {
        this.configSendMailId = configSendMailId;
    }

    public String getMailList() {
        return mailList;
    }

    public void setMailList(String mailList) {
        this.mailList = mailList;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
}
