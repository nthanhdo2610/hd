package com.tinhvan.hd.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="customer_forgot_password_token")
public class CustomerForgotPasswordToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @SequenceGenerator(name="customer_forgot_password_token_sequence",sequenceName="customer_forgot_password_token_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="customer_forgot_password_token_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "customer_uuid", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID customerUuid;  //customer uuid

    @Column(name = "email", length = 50)
    private String email;   //check format

    @Column(name = "token", nullable = false)
    private String token;   //JWT

    @Column(name = "created_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date createAt;

    @Column(name = "expired_at")
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date expiredAt;

    @Column(name = "status", columnDefinition="SMALLINT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int status;   //0: disable, 1: enable

    public CustomerForgotPasswordToken() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CustomerForgotPasswordToken{" +
                "id=" + id +
                ", customerUuid=" + customerUuid +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", createAt=" + createAt +
                ", expiredAt=" + expiredAt +
                ", status=" + status +
                '}';
    }
}
