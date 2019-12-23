package com.tinhvan.hd.bean;

public class RoleAuthorizeListByRoleIdRespon {

    private int roleAuthorizeId;
    private int servicesId;
    private String crud;
    private String entryPoint;
    private String menu;
    private String action;
    private String parent;

    public RoleAuthorizeListByRoleIdRespon(int roleAuthorizeId, int servicesId, String crud,
                                           String entryPoint, String menu, String action, String parent) {
        this.roleAuthorizeId = roleAuthorizeId;
        this.servicesId = servicesId;
        this.crud = crud;
        this.entryPoint = entryPoint;
        this.menu = menu;
        this.action = action;
        this.parent = parent;
    }

    public int getRoleAuthorizeId() {
        return roleAuthorizeId;
    }

    public void setRoleAuthorizeId(int roleAuthorizeId) {
        this.roleAuthorizeId = roleAuthorizeId;
    }

    public int getServicesId() {
        return servicesId;
    }

    public void setServicesId(int servicesId) {
        this.servicesId = servicesId;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
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

    @Override
    public String toString() {
        return "roleAuthorizeId=" + roleAuthorizeId +
                ", servicesId=" + servicesId +
                ", crud='" + crud + '\'' +
                ", entryPoint='" + entryPoint + '\'' +
                ", menu='" + menu + '\'' +
                ", action='" + action + '\'' +
                ", parent='" + parent;
    }
}
