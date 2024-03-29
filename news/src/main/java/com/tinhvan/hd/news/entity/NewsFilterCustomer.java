package com.tinhvan.hd.news.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "news_filter_customer")
public class NewsFilterCustomer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="news_filter_customer_sequence",sequenceName="news_filter_customer_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="news_filter_customer_sequence")
    private int id;

    @Column(name = "news_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID newsId;

    @Column(name = "customer_filter_category_key", nullable = false)
    private String key;

    @Column(name = "compare_type_code", nullable = false)
    private String compare;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "CREATED_AT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt;

    @Column(name = "MODIFIED_AT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date modifiedAt;

    @Column(name = "CREATED_BY")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID createdBy;

    @Column(name = "MODIFIED_BY")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID modifiedBy;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
