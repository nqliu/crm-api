package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.entity.Contract;
import com.crm.entity.Customer;
import com.crm.entity.Lead;
import com.crm.mapper.ContractMapper;
import com.crm.mapper.CustomerMapper;
import com.crm.mapper.LeadMapper;
import com.crm.service.DashboardService;
import com.crm.utils.DateUtils;
import com.crm.vo.DashboardResponse;
import com.crm.vo.StatisticsData;
import com.crm.vo.TrendData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 仪表盘服务实现类
 * 仪表盘服务实现类（含合同审核统计扩展）
 */
@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerMapper customerMapper;
    private final LeadMapper leadMapper;
    private final ContractMapper contractMapper;

    @Override
    public DashboardResponse getDashboardStatistics() {
        DashboardResponse response = new DashboardResponse();

        // 获取统计数据
        // 获取统计数据（含新增的审核统计）
        response.setStatistics(calculateStatistics());
        // 获取趋势数据
        // 获取趋势数据（含审核趋势）
        response.setTrend(getTrendData());

        return response;
    }

    /**
     * 计算当日统计数据及变化率
     * 计算当日统计数据及变化率（新增合同审核统计）
     */
    private StatisticsData calculateStatistics() {
        StatisticsData data = new StatisticsData();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // 计算新增客户数据
        // 原有：新增客户统计
        int todayCustomers = countCustomerByDate(today);
        int yesterdayCustomers = countCustomerByDate(yesterday);
        data.setNewCustomerCount(todayCustomers);
        data.setCustomerChange(calculateChangeRate(todayCustomers, yesterdayCustomers));

        // 计算新增线索数据
        // 原有：新增线索统计
        int todayLeads = countLeadByDate(today);
        int yesterdayLeads = countLeadByDate(yesterday);
        data.setNewLeadCount(todayLeads);
        data.setLeadChange(calculateChangeRate(todayLeads, yesterdayLeads));

        // 计算新增合同数据
        // 原有：新增合同统计
        int todayContracts = countContractByDate(today);
        int yesterdayContracts = countContractByDate(yesterday);
        data.setNewContractCount(todayContracts);
        data.setContractChange(calculateChangeRate(todayContracts, yesterdayContracts));

        // 计算合同金额数据
        // 原有：合同金额统计
        BigDecimal todayAmount = sumContractAmountByDate(today);
        BigDecimal yesterdayAmount = sumContractAmountByDate(yesterday);
        data.setContractAmount(todayAmount);
        data.setAmountChange(calculateAmountChangeRate(todayAmount, yesterdayAmount));

        // 新增：今日审核通过合同统计
        int todayApproved = countApprovedContractsByDate(today);
        int yesterdayApproved = countApprovedContractsByDate(yesterday);
        data.setTodayApprovedContractCount(todayApproved);
        data.setApprovedContractChange(calculateChangeRate(todayApproved, yesterdayApproved));

        // 新增：今日审核拒绝合同统计
        int todayRejected = countRejectedContractsByDate(today);
        int yesterdayRejected = countRejectedContractsByDate(yesterday);
        data.setTodayRejectedContractCount(todayRejected);
        data.setRejectedContractChange(calculateChangeRate(todayRejected, yesterdayRejected));

        return data;
    }

    /**
     * 获取近7日趋势数据
     * 获取近7日趋势数据（新增审核趋势）
     */
    private TrendData getTrendData() {
        TrendData trendData = new TrendData();
        List<String> dates = new ArrayList<>();
        List<Integer> customerData = new ArrayList<>();
        List<Integer> leadData = new ArrayList<>();
        List<Integer> contractData = new ArrayList<>();
        // 新增：审核通过/拒绝趋势数据列表
        List<Integer> approvedData = new ArrayList<>();
        List<Integer> rejectedData = new ArrayList<>();

        // 获取近7天日期
        // 获取近7天日期及对应数据
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dateStr);

            // 按日期查询各类数据
            // 原有趋势数据
            customerData.add(countCustomerByDate(date));
            leadData.add(countLeadByDate(date));
            contractData.add(countContractByDate(date));

            // 新增：审核趋势数据
            approvedData.add(countApprovedContractsByDate(date));
            rejectedData.add(countRejectedContractsByDate(date));
        }

        // 设置原有趋势数据
        trendData.setDates(dates);
        trendData.setCustomerData(customerData);
        trendData.setLeadData(leadData);
        trendData.setContractData(contractData);

        // 设置新增审核趋势数据
        trendData.setApprovedData(approvedData);
        trendData.setRejectedData(rejectedData);

        return trendData;
    }

    /**
     * 按日期统计客户数量
     */
    // ---------------------- 原有统计方法 ----------------------
    private int countCustomerByDate(LocalDate date) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        wrapper.apply("DATE(create_time) = {0}", date)
                .eq(Customer::getDeleteFlag, 0);
        // 过滤已删除数据
        return Math.toIntExact(customerMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计线索数量
     */
    private int countLeadByDate(LocalDate date) {
        LambdaQueryWrapper<Lead> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        wrapper.apply("DATE(create_time) = {0}", date)
                .eq(Lead::getDeleteFlag, 0);
        // 过滤已删除数据
        return Math.toIntExact(leadMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计合同数量
     */
    private int countContractByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        wrapper.apply("DATE(create_time) = {0}", date)
                .eq(Contract::getDeleteFlag, 0);
        // 过滤已删除数据
        return Math.toIntExact(contractMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计合同金额
     */
    private BigDecimal sumContractAmountByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Contract::getAmount)
                .apply("DATE(create_time) = {0}", date)
                .eq(Contract::getDeleteFlag, 0); // 过滤已删除数据

        List<Contract> contracts = contractMapper.selectList(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        for (Contract contract : contracts) {
            if (contract.getAmount() != null) {
                total = total.add(contract.getAmount());
            }
        }
        return total;
    }


    // ---------------------- 新增审核统计方法 ----------------------

    /**
     * 计算数量变化百分比
     * 按日期统计审核通过的合同数量（状态=2，参考Contract实体类状态定义）
     */
    private int countApprovedContractsByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(update_time) = {0}", date)
                // 按状态更新时间统计
                .eq(Contract::getStatus, 2)
                // 2=审核通过
                .eq(Contract::getDeleteFlag, 0);
        // 过滤已删除数据
        return Math.toIntExact(contractMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计审核拒绝的合同数量（状态=3，参考Contract实体类状态定义）
     */
    private int countRejectedContractsByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(update_time) = {0}", date)
                // 按状态更新时间统计
                .eq(Contract::getStatus, 3)
                // 3=审核未通过
                .eq(Contract::getDeleteFlag, 0);
        // 过滤已删除数据
        return Math.toIntExact(contractMapper.selectCount(wrapper));
    }

    // ---------------------- 工具方法 ----------------------
    private int calculateChangeRate(int today, int yesterday) {
        if (yesterday == 0) {
            return today > 0 ? 100 : 0;
        }
        return (int) ((today - yesterday) * 100.0 / yesterday);
    }

    /**
     * 计算金额变化百分比
     */
    private int calculateAmountChangeRate(BigDecimal today, BigDecimal yesterday) {
        if (yesterday.compareTo(BigDecimal.ZERO) == 0) {
            return today.compareTo(BigDecimal.ZERO) > 0 ? 100 : 0;
        }
        return today.subtract(yesterday)
                .multiply(new BigDecimal(100))
                .divide(yesterday, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}