package com.tinhvan.hd.entity;


import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTIFICATION_ACTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificationAction implements HDPayload, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "notification_action_sequence", sequenceName = "notification_action_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_action_sequence")
    private int id;

    @Basic
    @Column(name = "notification_id", columnDefinition = "SMALLINT")
    private Integer notificationId;

    @Basic
    @Column(name = "TEMPLATE_TYPE", columnDefinition = "SMALLINT")
    private Integer templateType;

    @Basic
    @Column(name = "STATUS", columnDefinition = "SMALLINT")
    private Integer status = 1;

    @Basic
    @Column(name = "IS_SENT", columnDefinition = "SMALLINT")
    private Integer isSent;

    @Basic
    @Column(name = "IS_READ", columnDefinition = "SMALLINT")
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
    @Column(name = "title", length = 128)
    private String title;

    @Basic
    @Column(name = "CONTENT_PARA", columnDefinition = "VARCHAR(128)[]")
    private String[] contentPara;

    @Basic
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @Basic
    @Column(name = "NEWS_ID")
    private UUID newsId;

    @Basic
    @Column(name = "PROMOTION_ID")
    private UUID promotionId;

    @Basic
    @Column(name = "type", columnDefinition = "SMALLINT")
    private Integer type;

    @Column(name = "lang_code", length = 2)
    private String langCode;

    @Basic
    @Column(name = "access", columnDefinition = "SMALLINT")
    private Integer access;

    @Basic
    @Column(name = "is_deleted", columnDefinition = "SMALLINT")
    private Integer isDeleted;

    @Basic
    @Column(name = "deleted_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedTime;

    @Basic
    @Column(name = "action_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionAt;

    @Basic
    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    @Basic
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public NotificationAction() {
        super();
    }

    public NotificationAction(Notification notification) {
        setNotificationId(notification.getId());
        setTemplateType(notification.getTemplateType());
        setStatus(notification.getStatus());
        setIsSent(notification.getIsSent());
        setIsRead(notification.getIsRead());
        setSendTime(notification.getSendTime());
        setReadTime(notification.getReadTime());
        setCreatedAt(notification.getCreatedAt());
        setCustomerUuid(notification.getCustomerUuid());
        setContractUuid(notification.getContractUuid());
        setContentPara(notification.getContentPara());
        setContent(notification.getContent());
        setContent(notification.getContent());
        setNewsId(notification.getNewsId());
        setPromotionId(notification.getPromotionId());
        setLangCode(notification.getLangCode());
        setAccess(notification.getAccess());
        setFcmToken(notification.getFcmToken());
        setEndDate(notification.getEndDate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Date getActionAt() {
        return actionAt;
    }

    public void setActionAt(Date actionAt) {
        this.actionAt = actionAt;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void validatePayload() {

    }
}
