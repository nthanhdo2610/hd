package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT_FILE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_FILE_SEQ", allocationSize = 1)
public class ContractFile implements HDPayload,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "FILE_NAME",length = 300)
    private String fileName;

    @Basic
    @Column(name = "FILE_PATH",length = 300)
    private String filePath;

    @Basic
    @Column(name = "FILE_TYPE")
    private String fileType;

    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="idx")
    private int idx;

    @Column(name="file_size")
    private long fileSize;

    @Column(name="file_count_page")
    private int fileCountPage;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileCountPage() {
        return fileCountPage;
    }

    public void setFileCountPage(int fileCountPage) {
        this.fileCountPage = fileCountPage;
    }

    @Override
    public void validatePayload() {

    }
}
