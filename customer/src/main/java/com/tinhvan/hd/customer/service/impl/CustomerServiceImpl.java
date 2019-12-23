package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.customer.dao.CustomerDAO;
import com.tinhvan.hd.customer.model.Customer;
import com.tinhvan.hd.customer.model.CustomerLog;
import com.tinhvan.hd.customer.payload.StatisticsRegisterByDaysResponse;
import com.tinhvan.hd.customer.repository.CustomerLogRepository;
import com.tinhvan.hd.customer.repository.CustomerRepository;
import com.tinhvan.hd.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

//    @Autowired
//    private CustomerLogDAO customerLogDAO;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLogRepository customerLogRepository;

    @Override
    public void insert(Customer customer) {
        customerRepository.save(customer);
        //insert log
        customerLogRepository.save(new CustomerLog(customer));
    }

    @Override
    public void update(Customer customer) {
        customerRepository.save(customer);
        //insert log
        customerLogRepository.save(new CustomerLog(customer));
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findCustomerByUsername(username);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public Customer findByUuid(UUID uuid, int status) {
        return customerDAO.findByUuid(uuid, status);
    }

    @Override
    public List<Customer> find(List<UUID> customerIds, int pageNum, int pageSize, String oderBy, String direction) {
        return customerDAO.find(customerIds, pageNum, pageSize, oderBy, direction);
    }

    @Override
    public int count(List<UUID> customerIds) {
        return customerDAO.count(customerIds);
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Customer> findCustomerIdByFullNameOrEmail(String info, int pageNum, int pageSize, String oderBy, String direction){
        return customerDAO.findCustomerIdByFullNameOrEmail(info, pageNum, pageSize, oderBy, direction);
    }

    @Override
    public int countCustomerIdByFullNameOrEmail(String info) {
        return customerDAO.countCustomerIdByFullNameOrEmail(info);
    }

    @Override
    public int countRegister(int status) {
        //return customerDAO.countRegister(status);
        return customerRepository.countAllByStatusNot(status);
    }

    @Override
    public List<StatisticsRegisterByDaysResponse> statisticsRegisterByDays(int numOfDays) {
        return customerDAO.statisticsRegisterByDays(numOfDays);
    }
}
