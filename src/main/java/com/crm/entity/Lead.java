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
@TableName("t_lead")
@ApiModel(value = "Lead对象", description = "")
public class Lead {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("线索名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("客户级别")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("客户来源")
    @TableField("source")
    private Integer source;

    @ApiModelProperty("客户地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("跟进状态")
    @TableField("follow_status")
    private Integer followStatus;

    @ApiModelProperty("下次跟进时间")
    @TableField("next_follow_status")
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
    private String deleteFlag;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
