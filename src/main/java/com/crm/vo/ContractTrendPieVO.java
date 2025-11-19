package com.crm.vo;

import lombok.Data;

/**
 * 合同趋势饼图VO（按来源/类型等维度统计）
 */
@Data
public class ContractTrendPieVO {
    private Integer status;
    // 合同状态值
    private String statusName;
    // 合同状态名称
    private Integer count;
    // 数量（核心字段）
    private Double proportion;
    // 占比（百分比
}