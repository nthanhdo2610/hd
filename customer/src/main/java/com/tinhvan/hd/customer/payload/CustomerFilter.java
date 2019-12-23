package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPagination;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.customer.model.Customer;

import java.util.Date;
import java.util.List;

public class CustomerFilter implements HDPayload, HDPagination<Customer> {

    private Integer ageFrom;
    private Integer ageTo;
    private Short gender;
    private Integer province;
    private Integer district;

    private int limit;
    private String order;

    private Date nextCreatedAt;

    public CustomerFilter(){
        this.limit = 1;
        this.order = "CreatedAtDesc";
    }

    @Override
    public void validatePayload() {
    }

    public void next(List<Customer> ls) {
        Customer last = ls.get(ls.size() - 1);
        if (order.equals("CreatedAtAsc")||order.equals("CreatedAtDesc")) {
            this.nextCreatedAt = last.getCreatedAt();
        }
        this.nextCreatedAt = last.getCreatedAt();
    }

    public Object next() {
        if (order.equals("CreatedAtAsc")||order.equals("CreatedAtDesc")) {
            return this.nextCreatedAt;
        }
        return this.nextCreatedAt;
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "CustomerFilter{" +
                "ageFrom=" + ageFrom +
                ", ageTo=" + ageTo +
                ", gender=" + gender +
                ", province=" + province +
                ", district=" + district +
                ", limit=" + limit +
                ", order='" + order + '\'' +
                ", nextCreatedAt=" + nextCreatedAt +
                '}';
    }
}
