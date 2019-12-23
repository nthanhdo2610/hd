package com.tinhvan.hd.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LOG_CALL_PROCEDURE_MIDDLEDB")
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "LogCallProcedureMiddleDB_SEQ", allocationSize = 1)
public class LogCallProcedureMiddleDB {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Long id;

    @Basic
    @Column(name = "PROCEDURE_NAME")
    private String procedureName;

    @Basic
    @Column(name = "PARAMETER")
    private String parameter;

    @Basic
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Basic
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Basic
    @Column(name = "STATUS")
    private String status;

    @Basic
    @Column(name = "ERROR_STR")
    private String errorStr;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
