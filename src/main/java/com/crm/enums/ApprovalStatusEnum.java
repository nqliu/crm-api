package com.crm.enums;

import lombok.Getter;

/**
 *
 * @author alani
 */
@Getter
public enum ApprovalStatusEnum {
    INIT(0, "初始化"),
    APPROVED(1, "同意"),
    REJECTED(2, "拒绝");

    private final Integer value;
    private final String desc;

    ApprovalStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ApprovalStatusEnum getByValue(Integer value) {
        for (ApprovalStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}