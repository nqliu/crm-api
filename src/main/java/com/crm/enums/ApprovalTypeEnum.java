package com.crm.enums;
import lombok.Getter;

/**
 * @author alani
 */

@Getter
public enum ApprovalTypeEnum {
    CONTRACT(0, "合同审核"),
    PAYMENT(1, "回款审核");

    private final Integer value;
    private final String desc;

    ApprovalTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ApprovalTypeEnum getByValue(Integer value) {
        for (ApprovalTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}