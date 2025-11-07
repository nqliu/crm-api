package com.crm.service;

import com.crm.common.result.PageResult;
import com.crm.entity.Contract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.query.ContractQuery;
import com.crm.vo.ContractVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface ContractService extends IService<Contract> {
PageResult<ContractVO> getPage(ContractQuery query);
void saveOrUpdate(ContractVO contractVO);
}
