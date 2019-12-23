package com.tinhvan.hd.promotion.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Entity
@Table(name = "promotion_customer")
public class PromotionCustomer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "promotion_customer_sequence", sequenceName = "promotion_customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotion_customer_sequence")
    private int id;

    @Column(name = "promotion_id")
    private UUID promotionId;

    @Column(name = "CUSTOMER_ID")
    private UUID customerId;


    @Column(name = "TITLE", length = 128)
    private String title;

    @Column(name = "IMAGE_PATH", length = 300)
    private String imagePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getpromotionId() {
        return promotionId;
    }

    public void setpromotionId(UUID promotionId) {
        this.promotionId = promotionId;
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
