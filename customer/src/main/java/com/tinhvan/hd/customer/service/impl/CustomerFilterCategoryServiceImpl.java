package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerFilterCategoryDAO;
import com.tinhvan.hd.customer.model.CustomerFilterCategory;
import com.tinhvan.hd.customer.service.CustomerFilterCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerFilterCategoryServiceImpl implements CustomerFilterCategoryService {

    @Autowired
    private CustomerFilterCategoryDAO customerFilterCategoryDAO;

    @Override
    public List<CustomerFilterCategory> find() {
        return customerFilterCategoryDAO.find();
    }
}
