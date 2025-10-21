package com.crm.convert;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.crm.enums.FollowUpStatusEnum;

/**
 * @description:
 * @author: ycshang
 * @create: 2025-10-05 15:27
 **/
public class FollowUpStatusConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }


    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) {
        return FollowUpStatusEnum.getValueByName(context.getReadCellData().getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
        return new WriteCellData<>(FollowUpStatusEnum.getNameByValue(context.getValue()));
    }
}
