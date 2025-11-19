package com.crm.vo;

import lombok.Data;

/**
 * 仪表盘响应数据
 */
@Data
public class DashboardResponse {
    private StatisticsData statistics;
    private TrendData trend;
}