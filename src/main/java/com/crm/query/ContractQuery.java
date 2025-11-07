package com.crm.query;

import com.crm.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author HUAWEI
 */
@Data
public class ContractQuery extends Query {
    @Schema(description = "合同名称")
    private String name;
    @Schema(description = "客户id")
    private String customerId;
    @Schema(description = "合同编号")
    private String number;
    @Schema(description = "合同状态 0-合同初始化 1-审核中 2-审核通过 3-审核不通过")
    private Integer status;
}
