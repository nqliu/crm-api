package com.crm.controller;

import com.crm.common.aop.Log;
import com.crm.common.result.PageResult;
import com.crm.common.result.Result;
import com.crm.entity.Department;
import com.crm.enums.BusinessType;
import com.crm.query.DepartmentQuery;
import com.crm.query.IdQuery;
import com.crm.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("department")
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/page")
    @Operation(summary = "部门分页列表")
    @Log(title="部门列表-分页", businessType = BusinessType.SELECT)
    public Result<PageResult<Department>> getPage(@RequestBody @Validated DepartmentQuery query){
        return Result.ok(departmentService.getPage(query));
    }
    @PostMapping("list")
    @Operation(summary = "部门列表查询")
    public Result<List<Department>> getList() {
        return Result.ok(departmentService.getList());
    }
    @PostMapping("saveOrEdit")
    @Operation(summary = "保存或编辑部门")
    public Result saveOrEditDepartment(@RequestBody Department department) {
        departmentService.saveOrEditDepartment(department);
        return Result.ok();
    }
    @PostMapping("remove")
    @Operation(summary = "删除部门")
    public Result removeDepartment(@RequestBody @Validated IdQuery query) {
        departmentService.removeDepartment(query);
        return Result.ok();
    }
}