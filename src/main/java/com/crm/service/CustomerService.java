package com.crm.service;

import com.crm.common.result.PageResult;
import com.crm.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.Department;
import com.crm.query.CustomerQuery;
import com.crm.query.IdQuery;
import com.crm.vo.CustomerVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface CustomerService extends IService<Customer> {

    PageResult<CustomerVO> getPage(CustomerQuery query);
    void exportCustomer(CustomerQuery query, HttpServletResponse httpResponse);
    void saveOrUpdate(CustomerVO  customerVO);
    void removeCustomer(List<Integer> ids);
    void customerToPublicPool(IdQuery idQuery);
    void publicPoolToPrivate(IdQuery idQuery);

}
