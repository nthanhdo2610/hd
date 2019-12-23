package com.tinhvan.hd.customer.dao;

import com.tinhvan.hd.customer.model.CustomerImage;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomerImageDAO {
    //CustomerImage insert(CustomerImage image);
    //void update(CustomerImage image);
    void delete(UUID uuid, Date modifiedAt, int type);
    CustomerImage find(UUID uuid, String fileName, int type);
    CustomerImage findByType(UUID uuid, int type);

}
