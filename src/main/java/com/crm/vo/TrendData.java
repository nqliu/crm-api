package com.crm.vo;

import lombok.Data;
import java.util.List;

/**
 * 趋势数据VO
 */
@Data
public class TrendData {
    // 日期列表
    private List<String> dates;
    // 每日客户数据
    private List<Integer> customerData;
    // 每日线索数据
    private List<Integer> leadData;
    // 每日合同数据
    private List<Integer> contractData;
}