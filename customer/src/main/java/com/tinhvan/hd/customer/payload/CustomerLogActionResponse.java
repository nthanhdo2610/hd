package com.tinhvan.hd.customer.payload;
import com.tinhvan.hd.base.enities.CustomerLogAction;

import java.util.List;

public class CustomerLogActionResponse {
    private List<CustomerLogAction> list;
    private int count;

    public CustomerLogActionResponse(List<CustomerLogAction> list, int count) {
        this.list = list;
        this.count = count;
    }

    public List<CustomerLogAction> getList() {
        return list;
    }

    public void setList(List<CustomerLogAction> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CustomerLogActionResponse{" +
                "list=" + list +
                ", count=" + count +
                '}';
    }
}
