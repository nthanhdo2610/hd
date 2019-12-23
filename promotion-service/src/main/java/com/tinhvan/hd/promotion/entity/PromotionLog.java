package com.tinhvan.hd.promotion.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "promotion_log")
public class PromotionLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "promotion_log_sequence", sequenceName = "promotion_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotion_log_sequence")
    private int id;

    @Column(name = "promotion_id", length = 64, nullable = false)
    private UUID promotionId;

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

    @Column(name = "TYPE")
    private String type;

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

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "promotion_end_date")
    private Date promotionEndDate;

    public PromotionLog(Promotion promotion) {
        this.promotionId = promotion.getId();
        this.title = promotion.getTitle();
        this.contentBrief = promotion.getContentBrief();
        this.content = promotion.getContent();
        this.imagePath = promotion.getImagePath();
        this.access = promotion.getAccess();
        this.status = promotion.getStatus();
        this.type = promotion.getType();
        this.createdAt = promotion.getCreatedAt();
        this.modifiedAt = promotion.getModifiedAt();
        this.createdBy = promotion.getCreatedBy();
        this.modifiedBy = promotion.getModifiedBy();
        this.isFeatured = promotion.getIsFeatured();
        this.startDate = promotion.getStartDate();
        this.endDate = promotion.getEndDate();
        this.statusNotification = promotion.getStatusNotification();
        this.interestRate=promotion.getInterestRate();
        this.promotionEndDate=promotion.getPromotionEndDate();
    }

}
