package com.tinhvan.hd.service;

import com.tinhvan.hd.base.enities.CustomerLogAction;

public interface CustomerLogActionService {
    void createMQ(CustomerLogAction object);
}
