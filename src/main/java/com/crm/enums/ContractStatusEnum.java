package com.crm.enums;

import lombok.Getter;

/**
 *
 * @author alani
 */
@Getter
public enum ContractStatusEnum {
    INIT(0, "待审核"),
    UNDER_REVIEW(1, "审核中"),
    APPROVED(2, "审核通过"),
    REJECTED(3, "审核拒绝");

    private final Integer value;
    private final String desc;

    ContractStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ContractStatusEnum getByValue(Integer value) {
        for (ContractStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}