package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.convert.CustomerConvert;
import com.crm.entity.Customer;
import com.crm.entity.SysManager;
import com.crm.mapper.CustomerMapper;
import com.crm.query.CustomerQuery;
import com.crm.query.CustomerTrendQuery;
import com.crm.query.IdQuery;
import com.crm.security.user.SecurityUser;
import com.crm.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.utils.ExcelUtils;
import com.crm.vo.CustomerTrendVO;
import com.crm.vo.CustomerVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crm.utils.DateUtils.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Override
    public PageResult<CustomerVO> getPage(CustomerQuery query) {
        Page<CustomerVO> page = new Page<>(query.getPage(), query.getLimit());
        MPJLambdaWrapper<Customer> wrapper = selection(query);
        Page<CustomerVO> result = baseMapper.selectJoinPage(page, CustomerVO.class, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }
    @Override
    public void exportCustomer(CustomerQuery query, HttpServletResponse httpResponse){
        MPJLambdaWrapper<Customer> wrapper = selection(query);
        List<Customer> customerList = baseMapper.selectJoinList(wrapper);
        ExcelUtils.writeExcel(httpResponse, customerList,"客户信息","客户信息", CustomerVO.class);
    }
    private MPJLambdaWrapper<Customer> selection(CustomerQuery  query){
        MPJLambdaWrapper<Customer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Customer.class)
                .selectAs("o", SysManager::getAccount, CustomerVO::getOwnerName)
                .selectAs("c", SysManager::getAccount, CustomerVO::getCreaterName)
                .leftJoin(SysManager.class, "o", SysManager::getId, Customer::getOwnerId)
                .leftJoin(SysManager.class, "c", SysManager::getId, Customer::getCreaterId);
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like(Customer::getName, query.getName());
        }
        if (StringUtils.isNotBlank(query.getPhone())) {
            wrapper.like(Customer::getPhone, query.getPhone());
        }
        if (query.getLevel() != null) {
            wrapper.eq(Customer::getLevel, query.getLevel());
        }
        if (query.getSource() != null) {
            wrapper.eq(Customer::getSource, query.getSource());
        }
        if (query.getFollowStatus() != null) {
            wrapper.eq(Customer::getFollowStatus, query.getFollowStatus());
        }
        if (query.getIsPublic() != null) {
            wrapper.eq(Customer::getIsPublic, query.getIsPublic());
        }
        wrapper.orderByDesc(Customer::getCreateTime);
        return wrapper;
    }

    @Override
    public void saveOrUpdate(CustomerVO customerVO) throws ServerException {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>().eq(Customer::getName, customerVO.getPhone());
        if (customerVO.getId() == null){
            Customer customer = baseMapper.selectOne(wrapper);
            if (customer != null){
                throw new ServerException("该手机号客户已存在，请勿重复添加");
            }
            Customer convert = CustomerConvert.INSTANCE.convert(customerVO);
            Integer managerId = SecurityUser.getManagerId();
            convert.setOwnerId(managerId);
            convert.setCreaterId(managerId);
            baseMapper.insert(convert);
        }else{
            wrapper.ne(Customer::getId, customerVO.getId());
            Customer customer = baseMapper.selectOne(wrapper);
            if(customer!=null){
                throw new ServerException("该手机号客户已存在，请勿重复添加");
            }
            Customer convert = CustomerConvert.INSTANCE.convert(customerVO);
            baseMapper.updateById(convert);
        }
    }
    @Override
    public void removeCustomer(List<Integer> ids){
        removeByIds(ids);
    }

    @Override
    public void customerToPublicPool(IdQuery idQuery) throws ServerException {
        Customer customer = baseMapper.selectById(idQuery.getId());
        if(customer == null){
            throw new ServerException("客户不存在,无法转入公海");
        }
        customer.setIsPublic(1);
        customer.setOwnerId(null);
        baseMapper.updateById(customer);
    }

    @Override
    public void publicPoolToPrivate(IdQuery idQuery) throws ServerException {
        Customer customer = baseMapper.selectById(idQuery.getId());
        if (customer == null) {
            throw new ServerException("客户不存在,无法转入公海");
        }
        customer.setIsPublic(0);
        Integer ownerId = SecurityUser.getManagerId();
        customer.setOwnerId(ownerId);
        baseMapper.updateById(customer);
    }

    @Override
    public Map<String, List> getCustomerTrendData(CustomerTrendQuery query) {
        // 1、X轴展示的时间
        List<String> timeList = new ArrayList<>();
        // 2、Y轴展示的数据
        List<Integer> countList = new ArrayList<>();
        // 3、Mapper 查询返回的结果
        List<CustomerTrendVO> result;
        if ("day".equals(query.getTransactionType())){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime localDateTime =now.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime startTime = now.withHour(0).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<String> timeRange = new ArrayList<>();
            timeRange.add(formatter.format(startTime));
            timeRange.add(formatter.format(localDateTime));
            timeList = getHourData(timeRange);
            query.setTimeRange(timeRange);
            result = baseMapper.getTradeStatistics(query);
        }else if("monthrange".equals(query.getTransactionType())){
            query.setTimeFormat("%Y-%m");
            timeList = getMonthInRange(query.getTimeRange().get(0),query.getTimeRange().get(1));
            result = baseMapper.getTradeStatisticsByDay(query);
        }else if("week".equals(query.getTransactionType())){
            timeList = getWeekInRange(query.getTimeRange().get(0),query.getTimeRange().get(1));
            result = baseMapper.getTradeStatisticsByWeek(query);
        }else{
            query.setTimeFormat("%Y-%m-%d");
            timeList = getDatesInRange(query.getTimeRange().get(0),query.getTimeRange().get(1));
            result = baseMapper.getTradeStatisticsByDay(query);
        }
        //匹配时间点查询到的数据，没有值默认填充0
        List<CustomerTrendVO> finalResult = result;
        timeList.forEach((String time) -> {
            finalResult.stream().filter((CustomerTrendVO item) -> item.getTradeTime().equals(time)).findFirst().ifPresentOrElse((CustomerTrendVO item) -> {
                countList.add(item.getTradeCount());
            }, () -> {
                countList.add(0);
            });
        });

        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("timeList", timeList);
        resultMap.put("countList", countList);
        return resultMap;
    }
}