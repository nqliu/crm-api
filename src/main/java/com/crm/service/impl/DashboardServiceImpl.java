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
        response.setStatistics(calculateStatistics());
        // 获取趋势数据
        response.setTrend(getTrendData());

        return response;
    }

    /**
     * 计算当日统计数据及变化率
     */
    private StatisticsData calculateStatistics() {
        StatisticsData data = new StatisticsData();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // 计算新增客户数据
        int todayCustomers = countCustomerByDate(today);
        int yesterdayCustomers = countCustomerByDate(yesterday);
        data.setNewCustomerCount(todayCustomers);
        data.setCustomerChange(calculateChangeRate(todayCustomers, yesterdayCustomers));

        // 计算新增线索数据
        int todayLeads = countLeadByDate(today);
        int yesterdayLeads = countLeadByDate(yesterday);
        data.setNewLeadCount(todayLeads);
        data.setLeadChange(calculateChangeRate(todayLeads, yesterdayLeads));

        // 计算新增合同数据
        int todayContracts = countContractByDate(today);
        int yesterdayContracts = countContractByDate(yesterday);
        data.setNewContractCount(todayContracts);
        data.setContractChange(calculateChangeRate(todayContracts, yesterdayContracts));

        // 计算合同金额数据
        BigDecimal todayAmount = sumContractAmountByDate(today);
        BigDecimal yesterdayAmount = sumContractAmountByDate(yesterday);
        data.setContractAmount(todayAmount);
        data.setAmountChange(calculateAmountChangeRate(todayAmount, yesterdayAmount));

        return data;
    }

    /**
     * 获取近7日趋势数据
     */
    private TrendData getTrendData() {
        TrendData trendData = new TrendData();
        List<String> dates = new ArrayList<>();
        List<Integer> customerData = new ArrayList<>();
        List<Integer> leadData = new ArrayList<>();
        List<Integer> contractData = new ArrayList<>();

        // 获取近7天日期
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // 按日期查询各类数据
            customerData.add(countCustomerByDate(date));
            leadData.add(countLeadByDate(date));
            contractData.add(countContractByDate(date));
        }

        trendData.setDates(dates);
        trendData.setCustomerData(customerData);
        trendData.setLeadData(leadData);
        trendData.setContractData(contractData);

        return trendData;
    }

    /**
     * 按日期统计客户数量
     */
    private int countCustomerByDate(LocalDate date) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        return Math.toIntExact(customerMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计线索数量
     */
    private int countLeadByDate(LocalDate date) {
        LambdaQueryWrapper<Lead> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        return Math.toIntExact(leadMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计合同数量
     */
    private int countContractByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("DATE(create_time) = {0}", date);
        return Math.toIntExact(contractMapper.selectCount(wrapper));
    }

    /**
     * 按日期统计合同金额
     */
    private BigDecimal sumContractAmountByDate(LocalDate date) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Contract::getAmount)
                .apply("DATE(create_time) = {0}", date);

        List<Contract> contracts = contractMapper.selectList(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        for (Contract contract : contracts) {
            if (contract.getAmount() != null) {
                total = total.add(contract.getAmount());
            }
        }
        return total;
    }

    /**
     * 计算数量变化百分比
     */
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