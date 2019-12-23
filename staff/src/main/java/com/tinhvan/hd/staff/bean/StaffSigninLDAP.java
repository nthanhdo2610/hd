/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.bean;

/**
 *
 * @author LUUBI
 */
public class StaffSigninLDAP {

    private String displayName;
    private String primaryGroupID;

    public StaffSigninLDAP(String displayName, String primaryGroupID) {
        this.displayName = displayName;
        this.primaryGroupID = primaryGroupID;
    }

    public StaffSigninLDAP() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPrimaryGroupID() {
        return primaryGroupID;
    }

    public void setPrimaryGroupID(String primaryGroupID) {
        this.primaryGroupID = primaryGroupID;
    }

}
