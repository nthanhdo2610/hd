package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.enities.RoleEntity;

import java.util.List;

public class RoleListRespon {
    private List<RoleEntity> list;
    private int count;



    public RoleListRespon(List<RoleEntity> list, int count) {
        this.list = list;
        this.count = count;
    }

    public RoleListRespon() {
    }

    public List<RoleEntity> getList() {
        return list;
    }

    public void setList(List<RoleEntity> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
