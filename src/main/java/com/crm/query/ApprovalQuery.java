package com.crm.query;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author alani
 */
@Data
public class ApprovalQuery {
    @NotNull(message = "审核id不能为空")
    private Integer id;

    @NotNull(message = "审核状态不能为空")
    private Integer type;

    @ApiModelProperty("审核意见")
    @NotBlank(message = "审核意见不能为空")
    private String comment;
    // 新增审核意见字段
}