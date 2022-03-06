package com.task.elasticsearch.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileType {
    ARCHIVE,
    EMAIL,
    FILE,
    IMAGE;

    @JsonValue
    public String toValue() {
        return name();
    }


    //    private static Map map = new HashMap<>();

//    static {
//        for (OrderType orderType : OrderType.values()) {
//            map.put(orderType.id, orderType);
//        }
//    }
//
//    public static OrderType valueOf(int orderType) {
//        return (OrderType) map.get(orderType);
//    }
//
//    public String getValue() {
//        return map.get(id).toString();
//    }
}
