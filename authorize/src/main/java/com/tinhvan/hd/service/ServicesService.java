package com.tinhvan.hd.service;

import com.tinhvan.hd.model.Services;

import java.util.List;

public interface ServicesService {

    void insertServices(Services object);

    void update(Services object);

    List<Services> list();
}
