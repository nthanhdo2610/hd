package com.tinhvan.hd.entity;

import javax.persistence.*;

@Entity
@Table(name = "contract_file_position")
public class ContractFilePosition {
    @Id
    @SequenceGenerator(name = "contract_file_position_sequence", sequenceName = "contract_file_position_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_file_position_sequence")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "id")
    private int id;
    @Column(name = "value")
    private String value;
    @Column(name = "page")
    private int page;
    @Column(name = "point_x")
    private int pointX;
    @Column(name = "point_y")
    private int pointY;
    @Column(name = "file")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
