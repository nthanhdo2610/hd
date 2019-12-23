package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.dto.SchemeDto;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "scheme")
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SchemeDtoMapping",
                classes = @ConstructorResult(
                        targetClass = SchemeDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "schemeName", type = String.class),
                                @ColumnResult(name = "schemeValue", type = String.class),
                                @ColumnResult(name = "fileLink", type = String.class),
                                @ColumnResult(name = "createdAt", type = Date.class),
                                @ColumnResult(name = "createdBy", type = String.class),
                                @ColumnResult(name = "createdByName", type = String.class),
                                @ColumnResult(name = "modifiedAt", type = Date.class),
                                @ColumnResult(name = "modifiedBy", type = String.class)
                        }
                )
        )
})
public class Scheme implements HDPayload {
    @Id
    @SequenceGenerator(name = "scheme_sequence", sequenceName = "scheme_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheme_sequence")
    @Column(name = "id")
    private Long id;
    @Column(name = "scheme_name", length = 128)
    private String schemeName;
    @Column(name = "scheme_value", length = 1024)
    private String schemeValue;
    @Column(name = "file_link")
    private String fileLink;
    @Basic
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "created_by")
    private UUID createdBy;
    @Basic
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(name = "modified_by")
    private UUID modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeValue() {
        return schemeValue;
    }

    public void setSchemeValue(String schemeValue) {
        this.schemeValue = schemeValue;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
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
