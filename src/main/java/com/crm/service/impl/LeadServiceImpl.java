package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.convert.CustomerConvert;
import com.crm.entity.Customer;
import com.crm.entity.FollowUp;
import com.crm.entity.Lead;
import com.crm.mapper.CustomerMapper;
import com.crm.mapper.FollowUpMapper;
import com.crm.mapper.LeadMapper;
import com.crm.query.IdQuery;
import com.crm.query.LeadQuery;
import com.crm.security.user.SecurityUser;
import com.crm.service.LeadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springfox.documentation.oas.mappers.SecurityMapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
@AllArgsConstructor
public class LeadServiceImpl extends ServiceImpl<LeadMapper, Lead> implements LeadService {
    private final CustomerMapper customerMapper;
    private final FollowUpMapper followUpMapper;


    @Override
    public PageResult<Lead> getPage(LeadQuery query) {
        Page<Lead> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<Lead> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like(Lead::getName, query.getName());
        }
        if (query.getFollowStatus() != null) {
            wrapper.eq(Lead::getFollowStatus, query.getFollowStatus());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Lead::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(Lead::getCreateTime);
        Page<Lead> leadPage = baseMapper.selectPage(page, wrapper);

        return new PageResult<>(leadPage.getRecords(), leadPage.getTotal());
    }


    @Override
    public void saveOrEdit(Lead lead) {
        LambdaQueryWrapper<Lead> wrapper = new LambdaQueryWrapper<Lead>().eq(Lead::getName, lead.getName());
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>().eq(Customer::getPhone, lead.getPhone()));
        if (customer != null && lead.getStatus() == 1) {
            throw new ServerException("该手机号客户已经存在，请勿重复添加线索信息");
        }
        if (lead.getId() == null) {
            Lead selectLead = baseMapper.selectOne(wrapper);
            if (selectLead != null) {
                throw new ServerException("该线索名称已经存在，请勿重复添加线索信息");
            }
            lead.setOwnerId(SecurityUser.getManagerId());
            baseMapper.insert(lead);
        } else {
            wrapper.ne(Lead::getId, lead.getId());
            Lead selectLead = baseMapper.selectOne(wrapper);
            if (selectLead != null) {
                throw new ServerException("该线索名称已经存在，请勿重复添加线索信息");
            }
            if (lead.getStatus() == 1) {
                IdQuery idQuery = new IdQuery();
                idQuery.setId(lead.getId());
                convertToCustomer(idQuery);
            } else {
                customerMapper.delete(new LambdaQueryWrapper<Customer>().eq(Customer::getPhone, lead.getPhone()));
            }
            baseMapper.updateById(lead);
        }

    }

    @Override
    public void convertToCustomer(IdQuery idQuery) {
        Lead lead = baseMapper.selectById(idQuery.getId());
        if (lead == null) {
            throw new ServerException("该线索不存在,客户转化失败");
        }
//        1、将线索信息转化为客户，注意id设置为空，且设置创建人为当前的销售
        Customer customer = CustomerConvert.INSTANCE.leadConvert(lead);
        customer.setId(null);
        customer.setCreaterId(SecurityUser.getManagerId());
        customerMapper.insert(customer);
//       2、修改线索状态
        lead.setStatus(1);
        baseMapper.updateById(lead);
    }


    @Override
    public void followLead(FollowUp followUp) {
        Lead lead = baseMapper.selectById(followUp.getId());
        if (lead == null) {
            throw new ServerException("线索不存在,跟进失败");
        }
        lead.setNextFollowStatus(followUp.getNextFollowType());
        baseMapper.updateById(lead);
        followUp.setId(null);
        followUp.setCustomerId(lead.getId());
        followUp.setTargetType(1);
        followUpMapper.insert(followUp);
    }
}