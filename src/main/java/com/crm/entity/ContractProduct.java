package com.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * @since 2025-11-02
 */
@Getter
@Setter
@TableName("t_contract_product")
@ApiModel(value = "ContractProduct对象", description = "")
public class ContractProduct {

    @ApiModelProperty("主键自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品id")
    @TableField("p_id")
    private Integer pId;

    @ApiModelProperty("合同id")
    @TableField("c_id")
    private Integer cId;

    @ApiModelProperty("商品名称")
    @TableField("p_name")
    private String pName;

    @ApiModelProperty("商品价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("购买商品数量")
    @TableField("count")
    private Integer count;

    @ApiModelProperty("总价格")
    @TableField("total_price")
    private BigDecimal totalPrice;
}
