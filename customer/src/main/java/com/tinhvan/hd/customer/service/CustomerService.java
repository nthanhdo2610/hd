package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.Customer;
import com.tinhvan.hd.customer.payload.StatisticsRegisterByDaysResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    void insert(Customer customer);
    void update(Customer customer);
    Customer findByUsername(String username);
    Customer findByEmail(String email);
    Customer findByUuid(UUID uuid, int status);
    List<Customer> find(List<UUID> customerIds, int pageNum, int pageSize, String oderBy, String direction);
    int count(List<UUID> customerIds);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> findCustomerIdByFullNameOrEmail(String info, int pageNum, int pageSize, String oderBy, String direction);
    int countCustomerIdByFullNameOrEmail(String info);
    int countRegister(int status);
    List<StatisticsRegisterByDaysResponse> statisticsRegisterByDays(int numOfDays);
}