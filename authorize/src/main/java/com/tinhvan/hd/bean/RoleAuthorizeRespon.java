package com.tinhvan.hd.bean;

public class RoleAuthorizeRespon {
    private int roleAuthorizeId;
    private int servicesId;
    private String entryPoint;
    private String menu;
    private String crud;
    private String action;
    private int status;

    public RoleAuthorizeRespon(int roleAuthorizeId, int servicesId, String entryPoint, String menu, String crud, String action, int status) {
        this.roleAuthorizeId = roleAuthorizeId;
        this.servicesId = servicesId;
        this.entryPoint = entryPoint;
        this.menu = menu;
        this.crud = crud;
        this.action = action;
        this.status = status;
    }

    public int getServicesId() {
        return servicesId;
    }

    public void setServicesId(int servicesId) {
        this.servicesId = servicesId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRoleAuthorizeId() {
        return roleAuthorizeId;
    }

    public void setRoleAuthorizeId(int roleAuthorizeId) {
        this.roleAuthorizeId = roleAuthorizeId;
    }
}
