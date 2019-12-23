package com.tinhvan.hd.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notification_queue")
public class NotificationQueue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="notification_queue_sequence",sequenceName="notification_queue_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="notification_queue_sequence")
    private int id;

    @Column(name="customer_id")
    private UUID customerId;

    @Column(name = "fcm_token", length = 300)
    private String fcmToken;

    @Column(name = "title", length = 128)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", length = 2048)
    private String content;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private Integer status;

    @Column(name = "create_at")
    private Date createdAt;

    @Column(name = "send_at")
    private Date sendAt;

    @Column(name="news_id")
    private UUID newsId;

    @Column(name="promotion_id")
    private UUID promotionId;

    @Column(name = "lang_code", length = 2)
    private String langCode;

    @Column(name = "type",columnDefinition = "SMALLINT")
    private Integer type;

    @Basic
    @Column(name = "contract_uuid")
    private UUID contractUuid;

    @Basic
    @Column(name = "contract_code")
    private String contractCode;

    @Column(name = "access",columnDefinition = "SMALLINT")
    private Integer access;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
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

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }
    public static final class STATUS {
        public static final int NEW = 0;
        public static final int FAIL = -1;
        public static final int IGNORE = -2;
    }
}
