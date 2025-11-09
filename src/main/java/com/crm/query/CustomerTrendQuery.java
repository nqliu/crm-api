package com.crm.query;

import lombok.Data;

import java.util.List;
@Data
public class CustomerTrendQuery {
    // 时间数组
    private List<String> timeRange;
    // 时间类型
    private String transactionType;
    // 时间格式化类型
    private String timeFormat;
}
