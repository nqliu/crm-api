package com.crm.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author alani
 */
@Data
public class DashboardResponseDTO {
    private List<String> dates;
    private List<Integer> customerData;
    private List<Integer> leadData;
    private List<Integer> contractData;
}