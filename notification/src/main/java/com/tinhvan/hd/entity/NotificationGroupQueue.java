package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.util.VarCharStringArrayType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATION_GROUP_QUEUE")
@TypeDefs({
        @TypeDef(
                typeClass = VarCharStringArrayType.class,
                defaultForType = String[].class
        )
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "NOTIFICATION_QUEUE_SEQ", allocationSize = 1)
public class NotificationGroupQueue implements HDPayload, Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID", length = 32)
    private Integer id;

    @Basic
    @Column(name = "NOTIFICATION_GROUP_ID", length = 32)
    private Integer notificationGroupId;

    @Basic
    @Column(name = "NOTIFICATION_TYPE", columnDefinition = "SMALLINT")
    private Integer notificationType;

    @Basic
    @Column(name = "SEND_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime;

    @Basic
    @Column(name = "STATUS", columnDefinition = "SMALLINT")
    private Integer status = 0;

    @Basic
    @Column(name = "CONTENT_PARA", columnDefinition = "VARCHAR(128)[]")
    private String[] contentPara;

    @Basic
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getNotificationGroupId() {
        return notificationGroupId;
    }

    public void setNotificationGroupId(Integer notificationGroupId) {
        this.notificationGroupId = notificationGroupId;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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


    @Override
    public void validatePayload() {

    }
}
