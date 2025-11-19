package com.crm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductVO {
    @JsonProperty("pId")
    private Integer pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;
    private Integer count;
    private  BigDecimal price;
}
