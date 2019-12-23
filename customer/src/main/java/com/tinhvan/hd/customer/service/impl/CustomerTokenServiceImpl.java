package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerTokenDAO;
import com.tinhvan.hd.customer.model.CustomerToken;
import com.tinhvan.hd.customer.repository.CustomerTokenRepository;
import com.tinhvan.hd.customer.service.CustomerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerTokenServiceImpl implements CustomerTokenService {

    @Autowired
    private CustomerTokenDAO customertokenDAO;

    @Autowired
    private CustomerTokenRepository customerTokenRepository;

    @Override
    public void insert(CustomerToken customerToken) {
//        customertokenDAO.disable(
//                customerToken.getCustomerUuid(),
//                customerToken.getEnvironment(),
//                customerToken.getCreateAt());

        disableCustomerToken(customerToken.getCustomerUuid(), customerToken.getEnvironment(), customerToken.getCreateAt());

        customerTokenRepository.save(customerToken);
    }

    @Override
    public CustomerToken findByToken(String token) {
        return customerTokenRepository.findByToken(token);
    }

    @Override
    public void disable(UUID customerUuid, String environment, Date deletedAt) {

        disableCustomerToken(customerUuid, environment, deletedAt);
        //customertokenDAO.disable(customerUuid, environment, deletedAt);
    }

    @Override
    public void disableAllByCustomer(UUID customerUuid, Date deletedAt) {
        customertokenDAO.disableAllByCustomer(customerUuid, deletedAt);
    }

    public void disableCustomerToken(UUID customerUuid, String environment, Date deletedAt) {
        List<CustomerToken> customerTokens = customertokenDAO.getCustomerTokenByCustomerUuidAndEnvironments(customerUuid, environment);

        if (customerTokens != null && customerTokens.size() > 0) {
            for (CustomerToken customerToken : customerTokens) {
                customerToken.setDeletedAt(deletedAt);
                customerToken.setStatus(HDConstant.STATUS.DISABLE);
            }
            customerTokenRepository.saveAll(customerTokens);
        }
    }
}
