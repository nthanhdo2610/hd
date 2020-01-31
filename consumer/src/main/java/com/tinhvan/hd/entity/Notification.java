package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.util.VarCharStringArrayType;
import com.tinhvan.hd.vo.NotificationQueueVO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTIFICATION")
@TypeDefs({
        @TypeDef(
                typeClass = VarCharStringArrayType.class,
                defaultForType = String[].class
        )
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
public class Notification implements HDPayload, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID", length = 64)
    private Integer id;

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
    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    @Basic
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public Notification(NotificationQueueVO vo) {
        this.createdAt = new Date();
        this.title = vo.getTitle();
        this.content = vo.getContent();
        int type = vo.getType();
        if (type == HDConstant.NotificationType.PROMOTION)
            this.promotionId = vo.getUuid();
        if (type == HDConstant.NotificationType.NEWS || type == HDConstant.NotificationType.EVENT)
            this.newsId = vo.getUuid();
        this.customerUuid = vo.getCustomerId();
        this.type = vo.getType();
        this.langCode = vo.getLangCode();
        this.access = vo.getAccess();
        if (vo.getFcmTokens() != null)
            this.fcmToken = vo.getFcmTokens().toString();
        if (vo.getEndDate() != null)
            this.endDate = vo.getEndDate();
    }

    public Notification() {
        super();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", templateType=" + templateType +
                ", status=" + status +
                ", isSent=" + isSent +
                ", isRead=" + isRead +
                ", sendTime=" + sendTime +
                ", readTime=" + readTime +
                ", createdAt=" + createdAt +
                ", customerUuid=" + customerUuid +
                ", contractUuid=" + contractUuid +
                ", title='" + title + '\'' +
                ", contentPara=" + Arrays.toString(contentPara) +
                ", content='" + content + '\'' +
                ", newsId=" + newsId +
                ", promotionId=" + promotionId +
                ", type=" + type +
                ", langCode='" + langCode + '\'' +
                ", access=" + access +
                ", fcmToken='" + fcmToken + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
