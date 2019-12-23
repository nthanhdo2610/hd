package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDConstant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_image")
public class CustomerImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "customer_image_sequence", sequenceName = "customer_image_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_image_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "customer_uuid", nullable = false)
    private UUID uuid;

    @Column(name = "file_name", nullable = false, length = 300)
    private String fileName;

    @Column(name = "type", columnDefinition = "SMALLINT")
    private int type;

    @Column(name = "created_at")
    //@JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date createdAt;

    @Column(name = "modified_at")
    //@JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date modifiedAt;

    @Column(name = "active", columnDefinition = "SMALLINT")
    private int active;

    public CustomerImage() {
        super();
    }

    public void validatePayload() {
        if(this.uuid==null)
            throw new BadRequestException(1106, "invalid id");
    }

    public void init(Date now) {
        this.active = HDConstant.STATUS.ENABLE;
        this.createdAt = now;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static final class TYPE {
        public static final int AVATAR = 1;
        public static final int CMND_FRONT = 2;
        public static final int CMND_BACKSIDE = 3;
        public static final int PASSPORT = 4;
    }

    @Override
    public String toString() {
        return "CustomerImage{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", active=" + active +
                '}';
    }
}
