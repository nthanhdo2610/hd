package com.tinhvan.hd.model;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "config_staff")
public class ConfigStaff implements HDPayload {
    @Id
    @SequenceGenerator(name = "config_sequence", sequenceName = "config_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_sequence")
    @Column(name = "id")
    private int id;
    @Column(name = "key")
    private String key;
    @Column(name = "label")
    private String label;
    @Column(name = "value")
    private String value;
    @Column(name = "value_para")
    private String valuePara;
    @Column(name = "time_para")
    private String timePara;
    @Basic
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(name = "modified_by")
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValuePara() {
        return valuePara;
    }

    public void setValuePara(String valuePara) {
        this.valuePara = valuePara;
    }

    public String getTimePara() {
        return timePara;
    }

    public void setTimePara(String timePara) {
        this.timePara = timePara;
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
    public String toString() {
        return
                "id=" + id +
                ", key='" + key + '\'' +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", valuePara='" + valuePara + '\'' +
                ", timePara='" + timePara + '\'' +
                ", modifiedAt=" + modifiedAt +
                ", modifiedBy=" + modifiedBy;
    }

    @Override
    public void validatePayload() {

    }
}
