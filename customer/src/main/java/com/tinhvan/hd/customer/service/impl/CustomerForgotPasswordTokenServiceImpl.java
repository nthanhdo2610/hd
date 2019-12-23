package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerForgotPasswordTokenDAO;
import com.tinhvan.hd.customer.model.CustomerForgotPasswordToken;
import com.tinhvan.hd.customer.repository.CustomerForgotPasswordTokenRepository;
import com.tinhvan.hd.customer.service.CustomerForgotPasswordTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerForgotPasswordTokenServiceImpl implements CustomerForgotPasswordTokenService {
    @Autowired
    private CustomerForgotPasswordTokenDAO forgotPasswordTokenDAO;

    @Autowired
    private CustomerForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Override
    public void insert(CustomerForgotPasswordToken forgotPasswordToken) {

        List<CustomerForgotPasswordToken> customerForgotPasswordTokens = forgotPasswordTokenDAO.getForgotPasswordByCustomerUuidAndStatus(forgotPasswordToken.getCustomerUuid());

        if (customerForgotPasswordTokens != null && customerForgotPasswordTokens.size() > 0) {
            for (CustomerForgotPasswordToken item : customerForgotPasswordTokens) {
                item.setStatus(HDConstant.STATUS.DISABLE);
            }
            forgotPasswordTokenRepository.saveAll(customerForgotPasswordTokens);
            //forgotPasswordTokenDAO.delete(forgotPasswordToken);
        }


        forgotPasswordTokenRepository.save(forgotPasswordToken);
    }

    @Override
    public CustomerForgotPasswordToken findActive(String token) {
        return forgotPasswordTokenDAO.findActive(token);
    }

    @Override
    public void update(CustomerForgotPasswordToken forgotPasswordToken) {
        forgotPasswordTokenRepository.save(forgotPasswordToken);
    }
}
