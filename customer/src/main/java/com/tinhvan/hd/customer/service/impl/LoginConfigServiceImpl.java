package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.customer.dao.LoginConfigDAO;
import com.tinhvan.hd.customer.model.LoginConfig;
import com.tinhvan.hd.customer.repository.LoginConfigRepository;
import com.tinhvan.hd.customer.service.LoginConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class LoginConfigServiceImpl implements LoginConfigService {

    @Autowired
    private LoginConfigDAO loginConfigDAO;

    @Autowired
    private LoginConfigRepository loginConfigRepository;

    @Override
    public int find(int count) {
        return loginConfigDAO.find(count);
    }

    @Override
    public List<LoginConfig> findAll() {
        return (List<LoginConfig>) loginConfigRepository.findAll();
    }
}
