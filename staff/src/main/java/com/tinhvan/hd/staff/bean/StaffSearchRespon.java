package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.staff.model.Staff;

import java.util.List;

public class StaffSearchRespon {
    private List<StaffList> list;
    private int count;

    public StaffSearchRespon(List<StaffList> list, int count) {
        this.list = list;
        this.count = count;
    }

    public List<StaffList> getList() {
        return list;
    }

    public void setList(List<StaffList> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
