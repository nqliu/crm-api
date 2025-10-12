package com.crm.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_follow_up")
@ApiModel(value = "FollowUp对象", description = "")
public class FollowUp {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("0-客户跟进，1-线索跟进")
    @TableField("target_type")
    private Byte targetType;

    @ApiModelProperty("跟进客户id")
    @TableField("customer_id")
    private Integer customerId;

    @ApiModelProperty("跟进类型")
    @TableField("follow_type")
    private Integer followType;

    @ApiModelProperty("跟进内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("下次跟进时间")
    @TableField("next_follow_type")
    private LocalDateTime nextFollowType;

    @ApiModelProperty("逻辑删除0-未删除，1-已删除")
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
