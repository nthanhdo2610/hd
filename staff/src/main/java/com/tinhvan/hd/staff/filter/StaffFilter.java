/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.filter;

import com.tinhvan.hd.base.HDPagination;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.staff.model.Staff;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author LUUBI
 */
public class StaffFilter implements HDPayload, HDPagination<Staff> {

    private String key;
    private String department;
    private int role;
    private int limit;
    private String order;

    private Date nextCreatedAt;

//    yyyy-MM-dd HH:mm:ss.SSS
//    private Timestamp nextCreatedAt;


    public StaffFilter() {
//        this.nextCreatedAt = new Date();
        this.limit = 1;
        this.order = "CreatedAtAsc";
//        this.order = "CreatedAtAsc";
//        this.limit =1;
//        this.role = 1000;
    }

    @Override
    public void validatePayload() {
    }

    @Override
    public void next(List<Staff> ls) {
        Staff last = ls.get(ls.size() - 1);
        if (order.equals("CreatedAtAsc") || order.equals("CreatedAtDesc")) {
            this.nextCreatedAt = last.getCreatedAt();
        }
        this.nextCreatedAt = last.getCreatedAt();
    }

    @Override
    public Object next() {
        if (order.equals("CreatedAtAsc") || order.equals("CreatedAtDesc")) {
            return this.nextCreatedAt;
        }
        return this.nextCreatedAt;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
