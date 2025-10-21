package com.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum FollowUpStatusEnum {

    NEW_CUSTOMER(0, "新客"),
    WAITING_FOR_COMMUNICATION(1, "待再次沟通"),
    INTERESTED(2,"有意向"),
    CONVERTED_TO_OPPORTUNITY(3,"转入商机"),
    INVALID(4,"无效");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (FollowUpStatusEnum s : FollowUpStatusEnum.values()) {
            if (s.getValue() == value) {
                return s.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(String name) {
        for (FollowUpStatusEnum s : FollowUpStatusEnum.values()) {
            if (Objects.equals(s.getName(), name)) {
                return s.getValue();
            }
        }
        return null;
    }
}
