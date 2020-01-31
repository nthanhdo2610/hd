package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.news.entity.News;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class NewsRequest implements HDPayload {

    private String id;

    private String title;

    private String contentBrief;

    private String content;

    private String imagePath;

    private String imagePathBrief;

    private int access;

    private int status;

    private int type;

    private int isFeatured;

    private Date startDate;

    private Date endDate;

    private int statusNotification;

    private String linkShare;

    private String pathFilter;

    private String notificationContent;

    @Override
    public String toString() {
        return "NewsRequest{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contentBrief='" + contentBrief + '\'' +
                ", content='" + content + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imagePathBrief='" + imagePathBrief + '\'' +
                ", access=" + access +
                ", status=" + status +
                ", type=" + type +
                ", isFeatured=" + isFeatured +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", statusNotification=" + statusNotification +
                ", linkShare='" + linkShare + '\'' +
                ", pathFilter='" + pathFilter + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if (access <= 0)
            access = News.ACCESS.GENERAL;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
}
