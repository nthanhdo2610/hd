package com.tinhvan.hd.news.entity;

import javax.persistence.*;
import java.io.Serializable;
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


    @Column(name = "TITLE",length = 128)
    private String title;

    @Column(name = "IMAGE_PATH",length = 300)
    private String imagePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
