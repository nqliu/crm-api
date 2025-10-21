package com.crm.controller;

import com.crm.common.result.Result;
import com.crm.service.CommonService;

import com.crm.vo.FileUrlVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "通用模块")
@RestController
@RequestMapping("common")
@AllArgsConstructor
public class CommonController {
    private final CommonService commonService;

    @PostMapping("/upload/file")
    @Operation(summary = "上传文件")
    public Result<FileUrlVO> upload(@RequestBody MultipartFile file) {
        return Result.ok(commonService.upload(file));
    }
}
