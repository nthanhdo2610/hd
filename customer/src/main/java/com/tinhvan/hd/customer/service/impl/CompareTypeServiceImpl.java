package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.customer.dao.CompareTypeDAO;
import com.tinhvan.hd.customer.model.CompareType;
import com.tinhvan.hd.customer.service.CompareTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CompareTypeServiceImpl implements CompareTypeService {

    @Autowired
    private CompareTypeDAO compareTypeDAO;

    @Override
    public List<CompareType> find(){
        return compareTypeDAO.find();
    }
}
