package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.CustomerImage;

import java.util.UUID;

public interface CustomerImageService {
    CustomerImage insert(CustomerImage image);
    void update(CustomerImage image);
    CustomerImage find(UUID uuid, String fileName, int type);
    CustomerImage findByType(UUID uuid, int type);
}
