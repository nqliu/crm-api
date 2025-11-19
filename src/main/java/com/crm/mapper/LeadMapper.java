package com.crm.mapper;

import com.crm.entity.Lead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface LeadMapper extends BaseMapper<Lead> {
    int countByCreateDate(@Param("date") LocalDate date);
}
