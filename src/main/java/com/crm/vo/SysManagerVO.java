package com.crm.vo;

import com.crm.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author crm
 */
@Data
@Schema(description = "管理员")
public class SysManagerVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    private String nickname;


    @Schema(description = "状态 0-禁用，1-启用")
    private Integer status;

    @Schema(description = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "角色id")
    private Integer roleId;

    @Schema(description = "部门id")
    private Integer departId;

    @Schema(description = "状态 0：正常    1：停用", required = true)
    @Range(min = 0, max = 1, message = "用户状态不正确")
    private Integer isEnabled;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;

}