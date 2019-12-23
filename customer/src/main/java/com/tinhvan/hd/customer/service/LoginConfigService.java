package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.LoginConfig;

import java.util.List;

public interface LoginConfigService {
    int find(int count);
    List<LoginConfig> findAll();
}
