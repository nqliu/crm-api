package com.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomerLevelEnum {

    ORDINARY_CUSTOMER(0, "普通客户"),
    PREMIUM_CUSTOMER(1, "优质客户"),
    KEY_CUSTOMER(2,"重点客户"),
    OTHER(3,"其他");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (CustomerLevelEnum s : CustomerLevelEnum.values()) {
            if (s.getValue() == value) {
                return s.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(String name) {
        for (CustomerLevelEnum s : CustomerLevelEnum.values()) {
            if (Objects.equals(s.getName(), name)) {
                return s.getValue();
            }
        }
        return null;
    }
}
