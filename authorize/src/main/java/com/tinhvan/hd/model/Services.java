package com.tinhvan.hd.model;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Services implements HDPayload {
    @Id
    @SequenceGenerator(name = "services_sequence", sequenceName = "services_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "services_sequence")
    private int id;
    @Basic
    @Column(name = "entry_point", columnDefinition = "VARCHAR(128)")
    private String entryPoint;
    @Basic
    @Column(name = "menu", columnDefinition = "VARCHAR(128)")
    private String menu;
    @Basic
    @Column(name = "crud", columnDefinition = "VARCHAR(128)")
    private String crud;
    @Basic
    @Column(name = "action", columnDefinition = "VARCHAR(128)")
    private String action;
    @Basic
    @Column(name = "parent", columnDefinition = "VARCHAR(128)")
    private String parent;
    @Basic
    @Column(name = "idx")
    private int idx;
    @Basic
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic
    @Column(name = "created_by")
    private UUID createdBy;
    @Basic
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Basic
    @Column(name = "modified_by")
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public void validatePayload() {

    }
}
