package com.crm.controller;

import com.crm.common.aop.Log;
import com.crm.common.result.PageResult;
import com.crm.common.result.Result;
import com.crm.entity.Department;
import com.crm.entity.OperLog;
import com.crm.enums.BusinessType;
import com.crm.query.DepartmentQuery;
import com.crm.query.OperLogQuery;
import com.crm.service.DepartmentService;
import com.crm.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("operLog")
@AllArgsConstructor
public class OperLogController {
    private final OperLogService operLogService;

    @PostMapping("/page")
    @Operation(summary = "日志分页列表")
    @Log(title="日志列表-分页", businessType = BusinessType.SELECT)
    public Result<PageResult<OperLog>> getPage(@RequestBody @Validated OperLogQuery query){
        return Result.ok(operLogService.page(query));
    }
}