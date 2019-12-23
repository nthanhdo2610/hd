package com.tinhvan.hd.entity.enumtype;

import java.util.HashMap;
import java.util.Map;

public enum NotificationType {

    NOTIFICATION_CONTRACT_ONLINE(1),
    NOTIFICATION_CONTRACT_ONLINE_RETRY (2),
    NOTIFICATION_CONTRACT_ONLINE_SUCCESS_EDMC(31),
    NOTIFICATION_CONTRACT_ONLINE_SUCCESS_CL_BANK(32),
    NOTIFICATION_CONTRACT_ONLINE_SUCCESS_CL_NONE_BANK(33),
    NOTIFICATION_ADJUSTMENT_INFO_CONTRACT (4);

    private Integer value;

    private static Map<Integer, NotificationType> values = null;

    public Integer getValue() {
        return value;
    }

    NotificationType(Integer value) {
        this.value = value;
    }

    public static NotificationType parseValue(Integer value) {
        if (values == null) {
            values = new HashMap<Integer, NotificationType>(
                    NotificationType.values().length);
            for (NotificationType e : NotificationType.values())
                values.put(e.getValue(), e);
        }
        return values.get(value);
    }

    @Override
    public String toString(){
        return this.value == null? null : this.value.toString();
    }
}
