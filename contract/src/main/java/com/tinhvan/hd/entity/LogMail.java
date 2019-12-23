package com.tinhvan.hd.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log_mail")
public class LogMail {
    @Id
    @SequenceGenerator(name = "log_mail_sequence", sequenceName = "log_mail_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_mail_sequence")
    @Column(name = "id")
    private int id;

    @Column(name = "title",columnDefinition = "VARCHAR(512)")
    private String title;

    @Column(name = "email_type", columnDefinition = "VARCHAR(50)")
    private String emailType;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_file_attachment",columnDefinition = "TEXT")
    private int isFileAttachment;

    @Column(name = "mail_to",columnDefinition = "VARCHAR(256)")
    private String mailTo;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "is_sent")
    private int isSent;

    @Column(name = "error",columnDefinition = "TEXT")
    private String error;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsFileAttachment() {
        return isFileAttachment;
    }

    public void setIsFileAttachment(int isFileAttachment) {
        this.isFileAttachment = isFileAttachment;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LogMail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", emailType='" + emailType + '\'' +
                ", isFileAttachment=" + isFileAttachment +
                ", mailTo='" + mailTo + '\'' +
                ", createdAt=" + createdAt +
                ", isSent=" + isSent +
                ", error='" + error + '\'' +
                '}';
    }
}
