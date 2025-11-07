package com.crm.vo;


import com.crm.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: ycshang
 * @create: 2025-11-02 08:12
 **/
@Data
public class ContractVO {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("合同编号")
    private String number;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合同金额")
    private BigDecimal amount;

    @ApiModelProperty("已收到款项")
    private BigDecimal receivedAmount;

    @ApiModelProperty("签约时间")
    @NotNull(message = "合同签约时间不能为空")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private LocalDate signTime;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("商机id")
    private Integer opportunityId;

    @Schema(description = "合同状态 0-初始化，1-审核中，2-审核通过，3-审核未通过")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人id")
    private Integer createrId;

    @ApiModelProperty("签约人id")
    private Integer ownerId;

    @ApiModelProperty("合同开始时间")
    @NotNull(message = "合同开始时间不能为空")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private LocalDate startTime;

    @ApiModelProperty("合同结束时间")
    @NotNull(message = "合同结束时间不能为空")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private LocalDate endTime;

    List<ProductVO> products;
}
