package com.crm.vo;

import lombok.Data;
import org.apache.poi.ss.formula.functions.Count;
@Data
public class CustomerTrendVO {
    private String tradeTime;
    private Integer tradeCount;
}
