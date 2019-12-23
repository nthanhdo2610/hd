package com.tinhvan.hd.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTIFICATION_REMOVE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificationRemove implements Serializable {

    @Id
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "TEMPLATE_TYPE",columnDefinition = "SMALLINT")
    private Integer templateType;

    @Basic
    @Column(name = "STATUS",columnDefinition = "SMALLINT")
    private Integer status = 1;

    @Basic
    @Column(name = "IS_SENT",columnDefinition = "SMALLINT")
    private Integer isSent;

    @Basic
    @Column(name = "IS_READ",columnDefinition = "SMALLINT")
    private Integer isRead;

    @Basic
    @Column(name = "SEND_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime;

    @Basic
    @Column(name = "READ_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @Basic
    @Column(name = "CUSTOMER_UUID")
    private UUID customerUuid;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "CONTENT_PARA",columnDefinition="VARCHAR(128)[]")
    private String[] contentPara;


    @Basic
    @Column(name = "CONTENT",columnDefinition = "TEXT")
    private String content;

    public NotificationRemove(Notification notification) {
        setId(notification.getId());
        setContent(notification.getContent());
        setContentPara(notification.getContentPara());
        setContractUuid(notification.getContractUuid());
        setCreatedAt(notification.getCreatedAt());
        setCustomerUuid(notification.getCustomerUuid());
        setIsRead(notification.getIsRead());
        setIsSent(notification.getIsSent());
        setReadTime(notification.getReadTime());
        setStatus(notification.getStatus());
        setSendTime(notification.getSendTime());
        setTemplateType(notification.getTemplateType());
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String[] getContentPara() {
        return contentPara;
    }

    public void setContentPara(String[] contentPara) {
        this.contentPara = contentPara;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
