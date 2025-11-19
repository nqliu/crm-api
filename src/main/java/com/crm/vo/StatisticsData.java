package com.crm.vo;



import lombok.Data;
import java.math.BigDecimal;

/**
 * 仪表盘统计数据VO
 * @author alani
 */
@Data
public class StatisticsData {
    // 新增客户数量
    private int newCustomerCount;
    // 客户数量变化百分比
    private int customerChange;
    // 新增线索数量
    private int newLeadCount;
    // 线索数量变化百分比
    private int leadChange;
    // 新增合同数量
    private int newContractCount;
    // 合同数量变化百分比
    private int contractChange;
    // 合同总金额
    private BigDecimal contractAmount;
    // 金额变化百分比
    private int amountChange;
}