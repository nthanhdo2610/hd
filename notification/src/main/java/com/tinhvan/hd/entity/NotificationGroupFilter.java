package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTIFICATION_GROUP_FILTER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "NOTIFICATION_GROUP_FILTER_SEQ", allocationSize = 1)
public class NotificationGroupFilter implements HDPayload, Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 32)
    private Integer id;

    @Basic
    @Column(name = "NOTIFICATION_GROUP_ID",length = 32)
    private Integer notificationGroupId;

    @Basic
    @Column(name = "KEY",length = 50)
    private String key;

    @Basic
    @Column(name = "VALUE",length = 50)
    private String value;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getNotificationGroupId() {
        return notificationGroupId;
    }

    public void setNotificationGroupId(Integer notificationGroupId) {
        this.notificationGroupId = notificationGroupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void validatePayload() {

    }
}
