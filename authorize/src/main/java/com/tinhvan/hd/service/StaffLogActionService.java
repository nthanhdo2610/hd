package com.tinhvan.hd.service;

import com.tinhvan.hd.base.enities.StaffLogAction;

public interface StaffLogActionService {
    void createMQ(StaffLogAction staffLogAction);
}
