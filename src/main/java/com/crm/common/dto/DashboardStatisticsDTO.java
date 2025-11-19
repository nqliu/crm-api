package com.crm.common.dto;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author alani
 */
@Data
public class DashboardStatisticsDTO {
    private int newCustomerCount;
    private int customerChange;
    private int newLeadCount;
    private int leadChange;
    private int newContractCount;
    private int contractChange;
    private BigDecimal contractAmount;
    private int amountChange;
}