package com.tinhvan.hd.staff.dao;

import com.tinhvan.hd.staff.model.StaffLogAction;

import java.util.List;

public interface StaffLogDAO {
    void createOrUpdate(StaffLogAction object);
    List<StaffLogAction> list();
}
