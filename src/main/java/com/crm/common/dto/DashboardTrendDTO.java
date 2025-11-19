package com.crm.common.dto;

import lombok.Data;

/**
 * @author alani
 */
@Data
public class DashboardTrendDTO {
    private DashboardStatisticsDTO statistics;
    private DashboardTrendDTO trend;
}