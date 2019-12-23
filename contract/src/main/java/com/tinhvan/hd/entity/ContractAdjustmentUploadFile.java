package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "contract_adjustment_upload_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContractAdjustmentUploadFile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "contract_adjustment_upload_file_sequence", sequenceName = "contract_adjustment_upload_file_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_adjustment_upload_file_sequence")
    private int id;

    @Column(name = "file_path", length = 300)
    private String filePath;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "is_send_mail", columnDefinition = "SMALLINT")
    private int sendMail;

    @Column(name = "receipt_mail_list", length = 300)
    private String receiptMailList;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;

    @Column(name = "send_date")
    private Date sendDate;

    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;

    @Column(name = "created_name")
    private String createdName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSendMail() {
        return sendMail;
    }

    public void setSendMail(int sendMail) {
        this.sendMail = sendMail;
    }

    public String getReceiptMailList() {
        return receiptMailList;
    }

    public void setReceiptMailList(String receiptMailList) {
        this.receiptMailList = receiptMailList;
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    @Override
    public String toString() {
        return "ContractAdjustmentUploadFile{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                ", sendMail=" + sendMail +
                ", receiptMailList='" + receiptMailList + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", sendDate=" + sendDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", createdName='" + createdName + '\'' +
                '}';
    }
}
