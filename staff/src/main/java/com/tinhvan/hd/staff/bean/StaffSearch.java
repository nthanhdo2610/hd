package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.base.HDPayload;

public class StaffSearch implements HDPayload {

    private String key;
    private String department;
    private int role;
    private int pageNum;
    private int pageSize;
    private String direction;

    public StaffSearch() {
        this.role = 0;
        this.pageNum = 0;
        this.pageSize = 0;
        this.direction = "DESC";

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public void validatePayload() {

    }
}
