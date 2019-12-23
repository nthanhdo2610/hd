package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.customer.model.Customer;

import java.util.List;

public class CustomerSearchResponse {

    @JsonProperty("list")
    private List<Customer> customers;

    @JsonProperty("total")
    private int total;

    public CustomerSearchResponse(List<Customer> customers, int total) {
        this.customers = customers;
        this.total = total;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CustomerSearchResponse{" +
                "customers=" + customers +
                ", total=" + total +
                '}';
    }
}
