package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.base.enities.StaffLogAction;

public interface StaffLogActionService {
    void createMQ(StaffLogAction staffLogAction);
}
