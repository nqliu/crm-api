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
@TableName("t_product")
@ApiModel(value = "Product对象", description = "")
public class Product {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("销量")
    @TableField("sales")
    private Integer sales;

    @ApiModelProperty("库存")
    @TableField("stock")
    private Integer stock;

    @ApiModelProperty("商品状态 0-初始化，1-上架，2-下架")
    @TableField("status")
    private Byte status;

    @ApiModelProperty("封面图")
    @TableField("cover_image")
    private String coverImage;

    @ApiModelProperty("商品简介")
    @TableField("description")
    private String description;

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
}
