package com.crm.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.crm.convert.*;
import com.crm.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@ContentRowHeight(105)
@ColumnWidth(24)
public class CustomerVO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "客户名称")
    @ExcelProperty("客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @Schema(description = "手机号")
    @ExcelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "客户级别")
    @ExcelProperty(value = "客户级别",converter = CustomerLevelConverter.class)
    @NotNull(message = "客户级别不能为空")
    private Integer level;

    @Schema(description = "客户来源")
    @ExcelProperty(value = "客户来源",converter = CustomerSourceConverter.class)
   @NotNull(message = "客户来源不能为空")
    private Integer source;

    @Schema(description = "客户地址")
    @ExcelProperty("客户地址")
    private String address;

    @Schema(description = "跟进状态")
    @ExcelProperty(value = "跟进状态",converter = FollowUpStatusConverter.class)
    private Integer followStatus;

    @Schema(description = "下次跟进时间")
    @ExcelProperty("下次跟进时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime nextFollowStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人id")
    private Integer createrId;

    @Schema(description = "创建人")
    private String createrName;

    @Schema(description = "是否转入公海 0-未转入公海，1-已转入公海")
    private Integer isPublic;

    @Schema(description = "客户所属的员工id")
    private Integer ownerId;

    @Schema(description = "客户所属的员工")
    private String ownerName;

    @Schema(description = "是否为关键决策人 0-是，1-否")
    @ExcelProperty(value="是否为关键决策人",converter = IsKeyDecisionMakerConverter.class)
    private Integer isKeyDecisionMaker;

    @Schema(description = "性别 0-男，1-女 2-保密")
    @ExcelProperty(value = "性别",converter = GenderConverter.class)
    private Integer gender;

    @Schema(description = "成交次数")
    private Integer dealCount;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;
}