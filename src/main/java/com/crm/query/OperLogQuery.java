package com.crm.query;

import com.crm.common.model.Query;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "操作日志查询参数")
public class OperLogQuery extends Query {
    @ApiModelProperty("操作人账号")
    private String operName;
    @ApiModelProperty("业务日志操作时间段")
    private List<Timestamp> operTime;
    @ApiModelProperty("接口Url(精确）")
    private String operUrl;
}