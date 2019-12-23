package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.customer.payload.CustomerLogActionResponse;
import com.tinhvan.hd.customer.payload.CustomerLogActionSearch;

import java.util.List;

public interface CustomerLogActionService {
    void createMQ(CustomerLogAction customerLogAction);
    void create(CustomerLogAction customerLogAction);
    List<CustomerLogActionResponse> search(CustomerLogActionSearch object);
}
