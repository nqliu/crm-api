package com.crm.utils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.crm.common.exception.ServerException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
* @description: excel 写入工具类
*
* @author: ycshang
*
* @create: 2025-10-19 10:37
**/
public class ExcelUtils {
    /**
     * 导出 excel
     *
     * @param response
     * @param data      输出数据
     * @param fileName  excel 文件名称
     * @param sheetName excel sheet 名称
     * @param clazz     输出数据的模板
     */
    public static void writeExcel(HttpServletResponse response, List<? extends Object> data, String fileName, String sheetName, Class clazz) {
        //        表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont writeFont = new WriteFont();
        writeFont.setFontHeightInPoints((short) 12);
        //        设置表头样式居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setWriteFont(writeFont);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //        内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //        设置内容居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setWrapped(true);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        try {
            EasyExcel.write(getOutputStream(fileName, response), clazz).excelType(ExcelTypeEnum.XLS).sheet(sheetName).registerWriteHandler(horizontalCellStyleStrategy).doWrite(data);
        } catch (Exception e) {
            throw new ServerException(fileName + "文件导出失败");
        }

    }

    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        return response.getOutputStream();
    }
}

