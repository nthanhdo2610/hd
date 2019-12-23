package com.tinhvan.hd.service;

import com.tinhvan.hd.model.ConfigContractTypeBackground;

import java.util.List;

public interface ConfigContractTypeBackgroundService {

    ConfigContractTypeBackground insertOrUpdate(ConfigContractTypeBackground object);

    List<ConfigContractTypeBackground> list();

    ConfigContractTypeBackground findById(int id);

    ConfigContractTypeBackground findByContractType(String contractType);
}
