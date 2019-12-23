package com.tinhvan.hd.entity.enumtype;

import java.util.HashMap;
import java.util.Map;

public enum  ContractStatus {

    LIVE("LIVE"),

    MATURED("MATURED"),

    DUE("DUE"),


    OVERDUE_PAYMENT("OVERDUE_PAYMENT"),

    //cho ky
    WAITING_FOR_SIGNING("CONTRACT_PRINTING"),

    // cho duyet
    SIGNED_CONTRACT("SIGNED_CONTRACT");


    private String value;

    private static Map<String, ContractStatus> values = null;

    public String getValue() {
        return value;
    }

    ContractStatus(String value) {
        this.value = value;
    }

    public static ContractStatus parseValue(String value) {
        if (values == null) {
            values = new HashMap<String, ContractStatus>(
                    ContractStatus.values().length);
            for (ContractStatus e : ContractStatus.values())
                values.put(e.getValue(), e);
        }
        return values.get(value);
    }

    @Override
    public String toString() {
        return this.value == null ? null : this.value.toString();
    }
}
