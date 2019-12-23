package com.tinhvan.hd.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "send_mail_log_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SendMailLogAction  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "send_mail_log_action_sequence", sequenceName = "send_mail_log_action_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "send_mail_log_action_sequence")
    private int id;

    @Column(name = "object_name")
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "status_code", columnDefinition = "SMALLINT")
    private int status;

    @Column(name = "error_description")
    @Lob
    private String errorDescription;

    @Column(name = "content")
    @Lob
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
