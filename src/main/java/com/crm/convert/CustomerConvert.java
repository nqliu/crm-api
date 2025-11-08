package com.crm.convert;

import com.crm.entity.Customer;
import com.crm.entity.Lead;
import com.crm.vo.CustomerVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author alani
 */
@Mapper
public interface CustomerConvert {
    CustomerConvert INSTANCE = Mappers.getMapper(CustomerConvert.class);

    Customer convert(CustomerVO customerVO);

    Customer leadConvert(Lead lead);
}