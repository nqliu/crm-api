package com.crm.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Getter
@Setter
@TableName("t_contract")
@ApiModel(value = "Contract对象", description = "")
public class Contract {

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("合同编号")
    @TableField("number")
    private String number;

    @ApiModelProperty("合同名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("合同金额")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty("已收到款项")
    @TableField("received_amount")
    private BigDecimal receivedAmount;

    @ApiModelProperty("签约时间")
    @TableField("sign_time")
    private LocalDateTime signTime;

    @ApiModelProperty("客户id")
    @TableField("customer_id")
    private Integer customerId;

    @ApiModelProperty("商机id")
    @TableField("opportunity_id")
    private Integer opportunityId;

    @ApiModelProperty("合同状态 0-初始化，1-审核通过，2-审核未通过")
    @TableField("status")
    private Byte status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("逻辑删除 0-未删除，1-已删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Byte deleteFlag;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人id")
    @TableField("creater_id")
    private Integer createrId;

    @ApiModelProperty("签约人id")
    @TableField("owner_id")
    private Integer ownerId;

    @ApiModelProperty("合同开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty("合同结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;
}
