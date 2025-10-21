package com.crm.query;

import com.crm.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerQuery extends Query {
    @Schema(description = "客户名称")
    private String name;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "客户等级")
    private Integer level;
    @Schema(description = "客户来源")
    private Integer source;
    @Schema(description = "跟进状态")
    private Integer followStatus;
    @Schema(description = "0-客户列表，1-公海列表")
    private Integer isPublic;
}
