package com.crm.controller;

import com.crm.common.result.Result;
import com.crm.service.DashboardService;
import com.crm.vo.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 */
@Tag(name = "仪表盘管理")
@RestController
@RequestMapping("dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("getStatistics")
    @Operation(summary = "获取仪表盘统计数据")
    public Result<DashboardResponse> getStatistics() {
        return Result.ok(dashboardService.getDashboardStatistics());
    }
}