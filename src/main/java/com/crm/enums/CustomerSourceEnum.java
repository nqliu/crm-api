package com.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomerSourceEnum {

    PERSONAL_RESOURCE(0, "个人资源"),
    CUSTOMER_REFERRAL(1, "客户介绍"),
    OFFICIAL_WEBSITE(2,"官网咨询"),
    OFFICIAL_ACCOUNT(3,"公众号"),
    DOUYIN(4,"抖音"),
    XIAOHONGSHU(5,"小红书"),
    OTHER(6,"其他");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (CustomerSourceEnum s : CustomerSourceEnum.values()) {
            if (s.getValue() == value) {
                return s.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(String name) {
        for (CustomerSourceEnum s : CustomerSourceEnum.values()) {
            if (Objects.equals(s.getName(), name)) {
                return s.getValue();
            }
        }
        return null;
    }
}
