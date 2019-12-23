package com.tinhvan.hd.news.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NEWS_LOG")
public class NewsLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "news_log_sequence", sequenceName = "news_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_log_sequence")
    private int id;

    @Column(name = "news_id", length = 64, nullable = false)
    private UUID newsId;

    @Column(name = "TITLE", length = 128, nullable = false)
    private String title;

    @Column(name = "CONTENT_BRIEF", length = 512, nullable = false)
    private String contentBrief;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "IMAGE_PATH", length = 300)
    private String imagePath;

    @Column(name = "ACCESS", columnDefinition = "SMALLINT")
    private Integer access;

    @Column(name = "STATUS", columnDefinition = "SMALLINT")
    private Integer status;

    @Column(name = "TYPE", columnDefinition = "SMALLINT")
    private Integer type;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;

    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;

    @Column(name = "IS_FEATURED", columnDefinition = "SMALLINT")
    private Integer isFeatured;

    @Column(name = "SHOW_START_DATE")
    private Date startDate;

    @Column(name = "SHOW_END_DATE")
    private Date endDate;

    @Column(name = "STATUS_SEND_NOTIFICATION", columnDefinition = "SMALLINT")
    private int statusNotification;

    public NewsLog(News news) {
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.contentBrief = news.getContentBrief();
        this.content = news.getContent();
        this.imagePath = news.getImagePath();
        this.access = news.getAccess();
        this.status = news.getStatus();
        this.type = news.getType();
        this.createdAt = news.getCreatedAt();
        this.modifiedAt = news.getModifiedAt();
        this.createdBy = news.getCreatedBy();
        this.modifiedBy = news.getModifiedBy();
        this.isFeatured = news.getIsFeatured();
        this.startDate = news.getStartDate();
        this.endDate = news.getEndDate();
        this.statusNotification = news.getStatusNotification();
    }

}
