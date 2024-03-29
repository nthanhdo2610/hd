package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.promotion.entity.Promotion;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class PromotionRequest implements HDPayload {

    private String id;

    private String title;

    private String contentBrief;

    private String content;

    private String imagePath;

    private String imagePathBrief;

    private int access;

    private int status;

    private String type;

    private int isFeatured;

    private Date startDate;

    private Date endDate;

    private int statusNotification;

    private double interestRate;

    private Date promotionEndDate;

    private String linkShare;

    private String pathFilter;

    private String notificationContent;

    private String promotionCode;

    @Override
    public String toString() {
        return "PromotionRequest{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contentBrief='" + contentBrief + '\'' +
                ", content='" + content + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imagePathBrief='" + imagePathBrief + '\'' +
                ", access=" + access +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", isFeatured=" + isFeatured +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", statusNotification=" + statusNotification +
                ", interestRate=" + interestRate +
                ", promotionEndDate=" + promotionEndDate +
                ", linkShare='" + linkShare + '\'' +
                ", pathFilter='" + pathFilter + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                ", promotionCode='" + promotionCode + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if (access <= 0)
            access = Promotion.ACCESS.GENERAL;
        if (!HDUtil.isNullOrEmpty(id))
            try {
                UUID.fromString(id);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }
        if (HDUtil.isNullOrEmpty(title))
            throw new BadRequestException(1300, "empty title");
        else if (title.length() > 128)
            throw new BadRequestException(1301, "title too long");

        if (HDUtil.isNullOrEmpty(contentBrief))
            throw new BadRequestException(1302, "empty contentBrief");
        else if (contentBrief.length() > 512)
            throw new BadRequestException(1303, "contentBrief too long");

        if (HDUtil.isNullOrEmpty(content))
            throw new BadRequestException(1304, "content title");
        /*else if (content.length() > 2048)
            throw new BadRequestException(1305, "content too long");*/
        if (!HDUtil.isNullOrEmpty(linkShare) && linkShare.length() > 300)
            throw new BadRequestException(1307, "link shared too long");

        if (!HDUtil.isNullOrEmpty(pathFilter) && pathFilter.length() > 300)
            throw new BadRequestException(1308, "path filter too long");

        if (!HDUtil.isNullOrEmpty(notificationContent) && notificationContent.length() > 255)
            throw new BadRequestException(1310, "notificationContent too long");

//        if (startDate != null) {
//            startDate = HDUtil.setBeginDay(startDate);
//        }
//        if (endDate != null) {
//            endDate = HDUtil.setEndDay(endDate);
//        }
//
//        if (promotionEndDate != null) {
//            promotionEndDate = HDUtil.setEndDay(promotionEndDate);
//        }

        if (HDUtil.isNullOrEmpty(promotionCode))
            throw new BadRequestException(1312, "promotion code is empty");
        else if (promotionCode.length() > 100)
            throw new BadRequestException(1311, "promotion code too long");

        if (imagePath == null)
            imagePath = "";
        if (imagePathBrief == null)
            imagePathBrief = "";
        if (pathFilter == null)
            pathFilter = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
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

    public void setPromotionEndDate(Date promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public String getLinkShare() {
        return linkShare;
    }

    public void setLinkShare(String linkShare) {
        this.linkShare = linkShare;
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
}
