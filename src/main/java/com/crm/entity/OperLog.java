package com.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Getter
@Setter
@TableName("sys_oper_log")
@ApiModel(value = "OperLog对象", description = "操作日志记录")
public class OperLog {

    @ApiModelProperty("日志主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("模块标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("业务类型（0其它 1新增 2修改 3删除）")
    @TableField("oper_type")
    private Integer operType;

    @ApiModelProperty("方法名称")
    @TableField("method")
    private String method;

    @ApiModelProperty("请求方式")
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty("操作人员")
    @TableField("oper_name")
    private String operName;

    @ApiModelProperty("操作人id")
    @TableField("manager_id")
    private String managerId;

    @ApiModelProperty("请求URL")
    @TableField("oper_url")
    private String operUrl;

    @ApiModelProperty("主机地址")
    @TableField("oper_ip")
    private String operIp;

    @ApiModelProperty("操作地点")
    @TableField("oper_location")
    private String operLocation;

    @ApiModelProperty("请求参数")
    @TableField("oper_param")
    private String operParam;

    @ApiModelProperty("返回参数")
    @TableField("json_result")
    private String jsonResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("错误消息")
    @TableField("error_msg")
    private String errorMsg;

    @ApiModelProperty("操作时间")
    @TableField("oper_time")
    private LocalDateTime operTime;

    @ApiModelProperty("消耗时间")
    @TableField("cost_time")
    private Long costTime;

    @ApiModelProperty("0-A平台，1-B平台")
    @TableField("oper_platform")
    private Integer operPlatform;
}
