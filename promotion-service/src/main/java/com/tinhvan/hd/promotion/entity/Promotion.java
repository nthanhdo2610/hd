package com.tinhvan.hd.promotion.entity;

import com.tinhvan.hd.promotion.payload.PromotionRequest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Entity
@Table(name = "promotion")
public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TITLE", length = 128, nullable = false)
    private String title;

    @Column(name = "CONTENT_BRIEF", length = 512, nullable = false)
    private String contentBrief;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "IMAGE_PATH", length = 300)
    private String imagePath;

    @Column(name = "IMAGE_PATH_BRIEF", length = 300)
    private String imagePathBrief;

    @Column(name = "ACCESS", columnDefinition = "SMALLINT")
    private Integer access;

    @Column(name = "STATUS", columnDefinition = "SMALLINT")
    private Integer status;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "IS_FEATURED", columnDefinition = "SMALLINT")
    private Integer isFeatured;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;

    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;

    @Column(name = "SHOW_START_DATE")
    private Date startDate;

    @Column(name = "SHOW_END_DATE")
    private Date endDate;

    @Column(name = "STATUS_SEND_NOTIFICATION", columnDefinition = "SMALLINT")
    private int statusNotification;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "promotion_end_date")
    private Date promotionEndDate;

    @Column(name = "link_share", length = 300)
    private String linkShare;

    @Column(name = "path_filter", length = 300)
    private String pathFilter;

    @Column(name = "notification_content")
    private String notificationContent;

    @Column(name = "promotion_code", length = 100)
    private String promotionCode;

    @Transient
    private String typeName;

    @Transient
    boolean validRegister;

    /*@Transient
    List<PromotionFilterCustomer> filterCustomers;*/

    public void init(PromotionRequest promotionRequest) {
        this.title = promotionRequest.getTitle();
        this.contentBrief = promotionRequest.getContentBrief();
        this.content = promotionRequest.getContent();
        this.imagePath = promotionRequest.getImagePath();
        this.imagePathBrief = promotionRequest.getImagePathBrief();
        this.access = promotionRequest.getAccess();
        this.status = promotionRequest.getStatus();
        this.type = promotionRequest.getType();
        this.isFeatured = promotionRequest.getIsFeatured();
        this.startDate = promotionRequest.getStartDate();
        this.endDate = promotionRequest.getEndDate();
        this.statusNotification = promotionRequest.getStatusNotification();
        this.interestRate = promotionRequest.getInterestRate();
        this.promotionEndDate = promotionRequest.getPromotionEndDate();
        this.linkShare = promotionRequest.getLinkShare();
        this.pathFilter = promotionRequest.getPathFilter();
        this.notificationContent = promotionRequest.getNotificationContent();
        this.promotionCode = promotionRequest.getPromotionCode();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentBrief() {
        return contentBrief;
    }

    public void setContentBrief(String contentBrief) {
        this.contentBrief = contentBrief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePathBrief() {
        return imagePathBrief;
    }

    public void setImagePathBrief(String imagePathBrief) {
        this.imagePathBrief = imagePathBrief;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public Integer getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatusNotification() {
        return statusNotification;
    }

    public void setStatusNotification(int statusNotification) {
        this.statusNotification = statusNotification;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Date getPromotionEndDate() {
        return promotionEndDate;
    }

    public String getLinkShare() {
        return linkShare;
    }

    public void setLinkShare(String linkShare) {
        this.linkShare = linkShare;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setPromotionEndDate(Date promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public String getPathFilter() {
        return pathFilter;
    }

    public void setPathFilter(String pathFilter) {
        this.pathFilter = pathFilter;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public boolean isValidRegister() {
        return validRegister;
    }

    public void setValidRegister(boolean validRegister) {
        this.validRegister = validRegister;
    }

    /*public List<PromotionFilterCustomer> getFilterCustomers() {
        return filterCustomers;
    }

    public void setFilterCustomers(List<PromotionFilterCustomer> filterCustomers) {
        this.filterCustomers = filterCustomers;
    }*/

    public static final class ACCESS {
        public static final int GENERAL = 1;
        public static final int INDIVIDUAL = 2;
    }

    public static final class STATUS_NOTIFICATION {
        public static final int NOT_SEND = 0;
        public static final int WILL_SEND = 1;
        public static final int WAS_SEND = 2;
    }

    public static final class FILE {
        public static final int IMAGE_PATH = 0;
        public static final int IMAGE_PATH_BRIEF = 1;
        public static final int PATH_FILTER = 2;
    }
}
