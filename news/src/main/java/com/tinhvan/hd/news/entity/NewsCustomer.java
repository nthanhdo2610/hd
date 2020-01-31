package com.tinhvan.hd.news.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Entity
@Table(name = "news_customer")
public class NewsCustomer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="news_customer_sequence",sequenceName="news_customer_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="news_customer_sequence")
    private int id;

    @Column(name = "NEWS_ID")
    private UUID newsId;

    @Column(name = "CUSTOMER_ID")
    private UUID customerId;

    @Column(name = "TITLE", length = 128)
    private String title;

    @Column(name = "notification_content", columnDefinition = "TEXT")
    private String notificationContent;

    @Column(name = "ACCESS", columnDefinition = "SMALLINT")
    private Integer access;

    @Column(name = "IMAGE_PATH", length = 300)
    private String imagePath;

    @Column(name = "contract_code", length = 128)
    private String contractCode;

    @Column(name = "STATUS_SEND_NOTIFICATION", columnDefinition = "SMALLINT")
    private int statusNotification;

    @Column(name = "STATUS", columnDefinition = "SMALLINT")
    private Integer status;

    @Column(name = "TYPE", columnDefinition = "SMALLINT")
    private Integer type;

    @Column(name = "SHOW_END_DATE")
    private Date endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getStatusNotification() {
        return statusNotification;
    }

    public void setStatusNotification(int statusNotification) {
        this.statusNotification = statusNotification;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
