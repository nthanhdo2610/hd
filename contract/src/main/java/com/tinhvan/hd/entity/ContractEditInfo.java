package com.tinhvan.hd.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_EDIT_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_EDIT_INFO_SEQ", allocationSize = 1)
public class ContractEditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "CONTRACT_CODE",length = 128)
    private String contractCode;

    @Basic
    @Column(name = "MONTHLY_DUE_DATE")
    private Integer monthlyDueDate;

    @Basic
    @Column(name = "IS_UPDATE_MONTHLY_DUE_DATE")
    private Integer isUpdateMonthlyDueDate;

    @Basic
    @Column(name = "IS_UPDATE_CHASSISNO_ENGINERNO")
    private Integer isUpdateChassinoEnginerno;

    @Basic
    @Column(name = "IS_UPDATE_CONPRINT_TO_DOCVERI")
    private Integer isUpdateConprintToDocveri;

    @Basic
    @Column(name = "IS_UPDATE_ADJUSTMENT")
    private Integer isUpdateAdjustment;


    @Basic
    @Column(name = "CHASSISNO",length = 128)
    private String chassisno;

    @Basic
    @Column(name = "ENGINERNO",length = 128)
    private String enginerno;

    @Basic
    @Column(name = "FIRST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstDate;

    @Basic
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Basic
    @Column(name = "UPDATE_MONTHLY_DUE_DATE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateMonthlyDueDateAt;


    @Basic
    @Column(name = "UPDATE_CHASSISNO_ENGINERNO_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateChassinoEnginernoAt;

    @Basic
    @Column(name = "update_conprint_to_docveri_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateConprintToDocveriAt;

    @Basic
    @Column(name = "update_adjustment_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAdjustmentAt;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(Integer monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public String getChassisno() {
        return chassisno;
    }

    public void setChassisno(String chassisno) {
        this.chassisno = chassisno;
    }

    public String getEnginerno() {
        return enginerno;
    }

    public void setEnginerno(String enginerno) {
        this.enginerno = enginerno;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getIsUpdateMonthlyDueDate() {
        return isUpdateMonthlyDueDate;
    }

    public void setIsUpdateMonthlyDueDate(Integer isUpdateMonthlyDueDate) {
        this.isUpdateMonthlyDueDate = isUpdateMonthlyDueDate;
    }

    public Integer getIsUpdateChassinoEnginerno() {
        return isUpdateChassinoEnginerno;
    }

    public void setIsUpdateChassinoEnginerno(Integer isUpdateChassinoEnginerno) {
        this.isUpdateChassinoEnginerno = isUpdateChassinoEnginerno;
    }

    public Integer getIsUpdateConprintToDocveri() {
        return isUpdateConprintToDocveri;
    }

    public void setIsUpdateConprintToDocveri(Integer isUpdateConprintToDocveri) {
        this.isUpdateConprintToDocveri = isUpdateConprintToDocveri;
    }

    public Integer getIsUpdateAdjustment() {
        return isUpdateAdjustment;
    }

    public void setIsUpdateAdjustment(Integer isUpdateAdjustment) {
        this.isUpdateAdjustment = isUpdateAdjustment;
    }

    public Date getUpdateMonthlyDueDateAt() {
        return updateMonthlyDueDateAt;
    }

    public void setUpdateMonthlyDueDateAt(Date updateMonthlyDueDateAt) {
        this.updateMonthlyDueDateAt = updateMonthlyDueDateAt;
    }

    public Date getUpdateChassinoEnginernoAt() {
        return updateChassinoEnginernoAt;
    }

    public void setUpdateChassinoEnginernoAt(Date updateChassinoEnginernoAt) {
        this.updateChassinoEnginernoAt = updateChassinoEnginernoAt;
    }

    public Date getUpdateConprintToDocveriAt() {
        return updateConprintToDocveriAt;
    }

    public void setUpdateConprintToDocveriAt(Date updateConprintToDocveriAt) {
        this.updateConprintToDocveriAt = updateConprintToDocveriAt;
    }

    public Date getUpdateAdjustmentAt() {
        return updateAdjustmentAt;
    }

    public void setUpdateAdjustmentAt(Date updateAdjustmentAt) {
        this.updateAdjustmentAt = updateAdjustmentAt;
    }
}
