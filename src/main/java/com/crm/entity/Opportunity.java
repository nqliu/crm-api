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
@TableName("t_opportunity")
@ApiModel(value = "Opportunity对象", description = "")
public class Opportunity {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商机名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("预算")
    @TableField("budget")
    private BigDecimal budget;

    @ApiModelProperty("关联客户id")
    @TableField("consumer_id")
    private Integer consumerId;

    @ApiModelProperty("商品id")
    @TableField("product_id")
    private Integer productId;

    @ApiModelProperty("预期成交时间")
    @TableField("expected_close_date")
    private LocalDateTime expectedCloseDate;

    @ApiModelProperty("下次跟进时间")
    @TableField("next_follow_time")
    private LocalDateTime nextFollowTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建人id")
    @TableField("creater_id")
    private Integer createrId;

    @ApiModelProperty("逻辑删除，0-未删除，1-已删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Byte deleteFlag;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
