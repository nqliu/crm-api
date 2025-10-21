package com.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.crm.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("t_product")
@ApiModel(value = "Product对象", description = "")
public class Product {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品名称")
    @TableField("name")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty("价格")
    @TableField("price")
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @ApiModelProperty("销量")
    @TableField("sales")
    private Integer sales;

    @ApiModelProperty("库存")
    @TableField("stock")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    @ApiModelProperty("商品状态 0-初始化，1-上架，2-下架")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("封面图")
    @TableField("cover_image")
    private String coverImage;

    @ApiModelProperty("商品简介")
    @TableField("description")
    private String description;

    @ApiModelProperty("逻辑删除 0-未删除，1-已删除")
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

    @ApiModelProperty("上架时间")
    @TableField(value = "on_shelf_time",updateStrategy = FieldStrategy.IGNORED)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN,timezone = "Asia/Shanghai")
    private LocalDateTime onShelfTime;

    @ApiModelProperty("下架时间")
    @TableField(value = "off_shelf_time",updateStrategy = FieldStrategy.IGNORED)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN,timezone = "Asia/Shanghai")
    private LocalDateTime offShelfTime;
}