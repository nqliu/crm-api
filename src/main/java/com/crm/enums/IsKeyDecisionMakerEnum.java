package com.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum IsKeyDecisionMakerEnum {

    YES(0, "是"),
    NO(1, "否");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (IsKeyDecisionMakerEnum s : IsKeyDecisionMakerEnum.values()) {
            if (s.getValue() == value) {
                return s.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(String name) {
        for (IsKeyDecisionMakerEnum s : IsKeyDecisionMakerEnum.values()) {
            if (Objects.equals(s.getName(), name)) {
                return s.getValue();
            }
        }
        return null;
    }
}
