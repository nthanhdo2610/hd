package com.tinhvan.hd.promotion.payload;

public class CustomerIdsByContractCode {

    private int idx;

    private String value;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomerIdsByContractCode{" +
                "idx=" + idx +
                ", value='" + value + '\'' +
                '}';
    }
}
