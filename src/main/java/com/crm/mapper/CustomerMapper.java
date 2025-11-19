package com.crm.mapper;

import com.crm.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.query.CustomerTrendQuery;
import com.crm.vo.CustomerTrendVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface CustomerMapper extends MPJBaseMapper<Customer> {
List<CustomerTrendVO> getTradeStatistics(@Param("query") CustomerTrendQuery query);
    List<CustomerTrendVO> getTradeStatisticsByDay(@Param("query") CustomerTrendQuery query);

    List<CustomerTrendVO> getTradeStatisticsByWeek(@Param("query") CustomerTrendQuery query);
    int countByCreateDate(@Param("date") LocalDate date);
}
