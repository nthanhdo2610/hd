/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.staff.model.Staff;

/**
 *
 * @author LUUBI
 */
public class StaffSigninRespon {
    private String token;
    private Staff staff;

    public StaffSigninRespon(String token, Staff staff) {
        this.token = token;
        this.staff = staff;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    
}
