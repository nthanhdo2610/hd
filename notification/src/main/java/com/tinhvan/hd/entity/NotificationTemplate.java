package com.tinhvan.hd.entity;


import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.vo.NotificationTemplateCreate;
import com.tinhvan.hd.vo.NotificationTemplateUpdate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTIFICATION_TEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "NOTIFICATION_TEMPLATE_SEQ", allocationSize = 1)
public class NotificationTemplate implements HDPayload ,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "TITLE")
    private String title;

    @Basic
    @Column(name = "BODY",length = 500)
    private String body;

    @Basic
    @Column(name = "TYPE",columnDefinition = "SMALLINT")
    private Integer type;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;


    @Basic
    @Column(name = "STATUS",columnDefinition = "SMALLINT")
    private Integer status = 1;

    @Basic
    @Column(name = "LANGCODE")
    private String langcode;

    @Basic
    @Column(name = "IS_DELETED",columnDefinition = "SMALLINT")
    private Integer isDeleted = 0;

    @Basic
    @Column(name = "DELETED_BY",columnDefinition = "UUID")
    private UUID deletedBy;

    @Basic
    @Column(name = "DELETED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Basic
    @Column(name = "STORED",columnDefinition = "SMALLINT")
    private Integer stored;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLangcode() {
        return langcode;
    }

    public void setLangcode(String langcode) {
        this.langcode = langcode;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
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

    public Integer getStored() {
        return stored;
    }

    public void setStored(Integer stored) {
        this.stored = stored;
    }

    public void setNotifiTemplateCreate(NotificationTemplateCreate notifiTemplateUpdate){
        this.setTitle(notifiTemplateUpdate.getTitle());
        this.setBody(notifiTemplateUpdate.getBody());
        this.setType(notifiTemplateUpdate.getType());
        this.setStatus(notifiTemplateUpdate.getStatus());
        this.setStored(notifiTemplateUpdate.getStored());
        this.setIsDeleted(notifiTemplateUpdate.getIsDeleted());

    }

    public void setNotifiTemplateUpdate(NotificationTemplateUpdate notifiTemplateUpdate){
        this.setId(notifiTemplateUpdate.getId());
        this.setTitle(notifiTemplateUpdate.getTitle());
        this.setBody(notifiTemplateUpdate.getBody());
        this.setType(notifiTemplateUpdate.getType());
        this.setStatus(notifiTemplateUpdate.getStatus());
        this.setStored(notifiTemplateUpdate.getStored());
        this.setIsDeleted(notifiTemplateUpdate.getIsDeleted());

    }


    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(body)){
            throw new BadRequestException(400, "body is empty");
        }

        if(HDUtil.isNullOrEmpty(title)){
            throw new BadRequestException(400, "title is empty");
        }
    }
}
