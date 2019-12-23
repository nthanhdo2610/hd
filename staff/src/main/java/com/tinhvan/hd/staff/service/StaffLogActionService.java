package com.tinhvan.hd.staff.service;

import com.tinhvan.hd.staff.model.StaffLogAction;

import java.util.List;

public interface StaffLogActionService {
    void createMQ(StaffLogAction object);
    List<StaffLogAction> list();
}
