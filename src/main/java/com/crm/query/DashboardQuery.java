package com.crm.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author alani
 */
@Data
@ApiModel(value = "统计查询参数")
public class DashboardQuery {
    @ApiModelProperty("统计日期（yyyy-MM-dd，默认今日）")
    private String date;
}