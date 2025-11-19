package com.crm.mapper;

import com.crm.entity.Contract;
import com.crm.vo.ContractTrendPieVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
public interface ContractMapper extends MPJBaseMapper<Contract> {
    // 按合同状态统计（对应 XML 中的 countByStatus 方法）
    List<ContractTrendPieVO> countByStatus(@Param("managerId") Integer managerId);
    int countByCreateDate(@Param("date") LocalDate date);
    BigDecimal sumAmountByCreateDate(@Param("date") LocalDate date);
    // 合同趋势
    int countTodayApprovedContracts(@Param("managerId") Integer managerId);
    int countTodayRejectedContracts(@Param("managerId") Integer managerId);
}