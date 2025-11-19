package com.crm.service;

import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.entity.Contract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.query.ApprovalQuery;
import com.crm.query.ContractQuery;
import com.crm.query.IdQuery;
import com.crm.vo.ContractTrendPieVO;
import com.crm.vo.ContractVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface ContractService extends IService<Contract> {
    //合同列表
    /**
     * 合同列表 - 分页
     * @param query
     * @return
     */
    PageResult<ContractVO> getPage(ContractQuery query);
    void saveOrUpdate(ContractVO contractVO) throws ServerException;

    /**
     * 新增
     */


    // 新增：按合同状态统计饼图数据
    List<ContractTrendPieVO> getContractStatusPieData();

    /**
     * 开启合同审批
     * @param idQuery
     */
    void startApproval(IdQuery idQuery);

    /**
     * 审批合同
     * @param query
     */
    void approvalContract(ApprovalQuery query);
    /**
     * 统计当日合同审核数量（首页用，需求3新增）
     * @return 当日审核通过/拒绝总数
     */
    Integer countTodayApprovalTotal();

}