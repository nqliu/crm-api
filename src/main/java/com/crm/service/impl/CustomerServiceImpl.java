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
import com.crm.vo.CustomerVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

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
    public void saveOrUpdate(CustomerVO  customerVO) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>().eq(Customer::getPhone,customerVO.getPhone());
        if(customerVO.getId()==null){
            Customer customer = baseMapper.selectOne(wrapper);
            if(customer!=null){
                throw new ServerException("该手机号已存在，请勿重复添加");
            }
            Customer convert = CustomerConvert.INSTANCE.convert(customerVO);
            Integer managerId = SecurityUser.getManagerId();
            convert.setCreaterId(managerId);
            convert.setOwnerId(managerId);
            baseMapper.insert(convert);
        }else {
            wrapper.ne(Customer::getId,customerVO.getId());
            Customer customer =baseMapper.selectOne(wrapper);
            if(customer!=null){
                throw new ServerException("该手机号已存在，请勿重复添加");
            }
            Customer convert = CustomerConvert.INSTANCE.convert(customerVO);
            baseMapper.updateById(convert);
        }

    }
    @Override
    public void removeCustomer(List<Integer>ids){
        removeByIds(ids);
    }
    @Override
    public void customerToPublicPool(IdQuery idQuery) {
        Customer customer = baseMapper.selectById(idQuery.getId());
        if (customer == null) {
            throw new ServerException("客户不存在，无法转入公海");
        }
        customer.setIsPublic(1);
        customer.setOwnerId(null);
        baseMapper.updateById(customer);
    }
    @Override
    public void publicPoolToPrivate(IdQuery idQuery) {
        Customer customer = baseMapper.selectById(idQuery.getId());
        if (customer == null) {
            throw new ServerException("客户不存在，无法转入公海");
        }
        customer.setIsPublic(0);
        Integer ownerId=SecurityUser.getManagerId();
        customer.setOwnerId(ownerId);
        baseMapper.updateById(customer);
    }

    @Override
    public Map<String, List> getCustomerTrend(CustomerTrendQuery query) {
        return Map.of();
    }
}