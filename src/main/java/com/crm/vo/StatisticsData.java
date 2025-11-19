package com.crm.vo;



import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("今日审核通过合同数量")
    private int todayApprovedContractCount;
    @ApiModelProperty("审核通过数量变化率(%)")
    private int approvedContractChange;
    @ApiModelProperty("今日审核拒绝合同数量")
    private int todayRejectedContractCount;
    @ApiModelProperty("审核拒绝数量变化率(%)")
    private int rejectedContractChange;
}