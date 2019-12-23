package com.tinhvan.hd.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "contract_send_file")
public class ContractSendFile implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "contract_send_file_sequence", sequenceName = "contract_send_file_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_send_file_sequence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "contract_uuid")
    private UUID contractUuid;

    @Column(name = "customer_uuid")
    private UUID customerUuid;

    @Column(name = "email", length = 127)
    private String email;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private int status;

    @Column(name = "created_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt;

    @Column(name = "file_name", length = 2048)
    private String fileName;

    @Column(name = "is_signed_adjustment", columnDefinition = "SMALLINT")
    private int isSignedAdjustment;
    @Column(name = "mail_type", columnDefinition = "VARCHAR(20)")
    private String mailType;

    public int getIsSignedAdjustment() {
        return isSignedAdjustment;
    }

    public void setIsSignedAdjustment(int isSignedAdjustment) {
        this.isSignedAdjustment = isSignedAdjustment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    @Override
    public String toString() {
        return "ContractSendFile{" +
                "id=" + id +
                ", contractUuid=" + contractUuid +
                ", customerUuid=" + customerUuid +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", fileName='" + fileName + '\'' +
                ", isSignedAdjustment=" + isSignedAdjustment +
                ", mailType='" + mailType + '\'' +
                '}';
    }
}
