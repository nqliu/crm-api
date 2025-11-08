package com.crm.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import com.crm.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@TableName("t_lead")
@ApiModel(value = "Lead对象", description = "")
public class Lead {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("线索名称")
    @TableField("name")
    @NotBlank(message = "线索名称不能为空")
    private String name;

    @ApiModelProperty("手机号")
    @TableField("phone")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("客户级别")
    @TableField("level")
    @NotNull(message = "客户级别不能为空")
    private Integer level;

    @ApiModelProperty("客户来源")
    @TableField("source")
    @NotNull(message = "客户来源不能为空")
    private Integer source;

    @ApiModelProperty("客户地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("跟进状态")
    @TableField("follow_status")
    @NotNull(message = "跟进状态不能为空")
    private Integer followStatus;

    @ApiModelProperty("下次跟进时间")
    @TableField("next_follow_status")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime nextFollowStatus;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("负责人id")
    @TableField("owner_id")
    private Integer ownerId;

    @ApiModelProperty("线索状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    @JsonIgnore
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime updateTime;
}