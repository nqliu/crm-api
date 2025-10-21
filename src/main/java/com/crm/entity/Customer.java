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
@TableName("t_customer")
@ApiModel(value = "Customer对象", description = "")
public class Customer {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("客户名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("客户级别 0-普通客户，1-优质客户，2-重点客户，3-其他")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("客户来源 0-个人资源，1-客户介绍，2-官网咨询，3-公众号，4-抖音，5-小红书，6-其他")
    @TableField("source")
    private Integer source;

    @ApiModelProperty("客户地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("跟进状态 0-新客，1-待再次沟通，2-有意向，3-转入商机，4-无效")
    @TableField("follow_status")
    private Integer followStatus;

    @ApiModelProperty("下次跟进时间")
    @TableField("next_follow_status")
    private LocalDateTime nextFollowStatus;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建人id")
    @TableField("creater_id")
    private Integer createrId;

    @ApiModelProperty("是否转入公海 0-未转入公海，1-已转入公海")
    @TableField("is_public")
    private Integer isPublic;

    @ApiModelProperty("客户所属的员工id")
    @TableField("owner_id")
    private Integer ownerId;

    @ApiModelProperty("是否为关键决策人 0-是，1-否")
    @TableField("is_key_decision_maker")
    private Integer isKeyDecisionMaker;

    @ApiModelProperty("性别 0-男，1-女 2-保密")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty("成交次数")
    @TableField("deal_count")
    private Integer dealCount;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
