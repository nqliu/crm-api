package com.crm.service.impl;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.convert.ContractConvert;
import com.crm.entity.Contract;
import com.crm.entity.ContractProduct;
import com.crm.entity.Customer;
import com.crm.entity.Product;
import com.crm.mapper.ContractMapper;
import com.crm.mapper.ContractProductMapper;
import com.crm.mapper.ProductMapper;
import com.crm.query.ContractQuery;
import com.crm.security.user.SecurityUser;
import com.crm.service.ContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.utils.NumberUtils;
import com.crm.vo.ContractVO;
import com.crm.vo.ProductVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.crm.utils.NumberUtils.generateContractNumber;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
@AllArgsConstructor
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    private  final ContractProductMapper contractProductMapper;
    private final ProductMapper productMapper;;
    @Override
    public PageResult<ContractVO> getPage(ContractQuery query) {
        Page<ContractVO> page = new Page<>();
        MPJLambdaWrapper<Contract> wrapper = new MPJLambdaWrapper<>();
        if (StringUtils.isNotBlank(query.getName())){
            wrapper.like(Contract::getName, query.getName());
        }
        if (query.getCustomerId()!=null){
            wrapper.eq(Contract::getCustomerId, query.getCustomerId());
        }
        if (StringUtils.isNotBlank(query.getNumber())) {
            wrapper.like(Contract::getNumber, query.getNumber());
        }

        if (query.getStatus() != null) {
            wrapper.eq(Contract::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(Contract::getCreateTime);
        //只查询目前登陆的员工签署的合同列表
        Integer managerId = SecurityUser.getManagerId();
        wrapper.selectAll(Contract.class)
                .selectAs(Customer::getName,ContractVO::getCustomerName)
                .leftJoin(Customer.class, Customer::getId, Contract::getCustomerId)
                .eq(Contract::getOwnerId, managerId);

        Page<ContractVO> result = baseMapper.selectJoinPage(page, ContractVO.class, wrapper);
        if (!result.getRecords().isEmpty()){
            result.getRecords().forEach(contractVO -> {
                List<ContractProduct> contractProducts = contractProductMapper.selectList(new LambdaQueryWrapper<ContractProduct>().eq(ContractProduct::getCId, contractVO.getId()));
                contractVO.setProducts(ContractConvert.INSTANCE.convertToProductVOList(contractProducts));
            });
        }
        return new PageResult<>(result.getRecords(),result.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ContractVO contractVO) throws ServerException {
        boolean isNew = contractVO.getId() == null;
        if (isNew && baseMapper.exists(new LambdaQueryWrapper<Contract>().eq(Contract::getName, contractVO.getName())))
            throw new ServerException("合同名称已存在");
        // 转换并保存合同关系
        Contract contract = ContractConvert.INSTANCE.convert(contractVO);
        contract.setCreaterId(SecurityUser.getManagerId());
        contract.setOwnerId(SecurityUser.getManagerId());
        if (isNew) {
            contract.setNumber(generateContractNumber());
            baseMapper.insert(contract);
        }else{
            Contract oldContract = baseMapper.selectById(contractVO.getId());
            if(oldContract==null){
                throw new ServerException("合同不存在");
            }
            if(oldContract.getStatus()==1){
                throw new ServerException("合同已审核通过，不能修改");
            }
            baseMapper.updateById(contract);
        }
        //处理商品和合同的关联关系

    }
    private void handleContractProducts(Integer contractId, List<ProductVO> newProductList) throws ServerException {
        if (newProductList == null) return;

        List<ContractProduct> oldProducts = contractProductMapper.selectList(
                new LambdaQueryWrapper<ContractProduct>().eq(ContractProduct::getCId, contractId)
        );

        // === 1. 新增商品 ===
        List<ProductVO> newAdded = newProductList.stream()
                .filter(np -> oldProducts.stream().noneMatch(op -> op.getPId().equals(np.getId())))
                .toList();
        for (ProductVO p : newAdded) {
            Product product = checkProductStock(p.getId(), p.getCount());
            decreaseStock(product, p.getCount());
            ContractProduct cp = buildContractProduct(contractId, product, p.getCount());
            contractProductMapper.insert(cp);
        }

        // === 2. 修改数量 ===
        List<ProductVO> changed = newProductList.stream()
                .filter(np -> oldProducts.stream()
                        .anyMatch(op -> op.getPId().equals(np.getId()) && !op.getCount().equals(np.getCount())))
                .toList();
        for (ProductVO p : changed) {
            ContractProduct old = oldProducts.stream()
                    .filter(op -> op.getPId().equals(p.getId()))
                    .findFirst().orElseThrow();

            Product product = checkProductStock(p.getId(), 0);
            int diff = p.getCount() - old.getCount();

            // 库存调整
            if (diff > 0) decreaseStock(product, diff);
            else if (diff < 0) increaseStock(product, -diff);

            // 更新合同商品
            old.setCount(p.getCount());
            old.setPrice(product.getPrice());
            old.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(p.getCount())));
            contractProductMapper.updateById(old);
        }

        // === 3. 删除商品 ===
        List<ContractProduct> removed = oldProducts.stream()
                .filter(op -> newProductList.stream().noneMatch(np -> np.getId().equals(op.getPId())))
                .toList();
        for (ContractProduct rm : removed) {
            Product product = productMapper.selectById(rm.getPId());
            if (product != null) increaseStock(product, rm.getCount());
            contractProductMapper.deleteById(rm.getId());
        }
    }


    private ContractProduct buildContractProduct(Integer contractId, Product product,int count){
        ContractProduct contractProduct = new ContractProduct();
        contractProduct.setCId(contractId);
        contractProduct.setPId(product.getId());
        contractProduct.setPName(product.getName());
        contractProduct.setPrice(product.getPrice());
        contractProduct.setCount(count);
        contractProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(count)));
        return contractProduct;
    }
    private Product checkProductStock(Integer productId,int count) throws ServerException {
        Product product = productMapper.selectById(productId);
        if (product == null){
            throw new ServerException("商品不存在");
        }
        if (product.getStock() < 0){
            throw new ServerException("商品库存不足");
        }
        return product;
    }
    private void increaseStock(Product product,int count){
        product.setStock(product.getStock()+count);
        product.setSales(product.getSales()+count);
        productMapper.updateById(product);
    }

    private void decreaseStock(Product product,int count){
        product.setStock(product.getStock()-count);
        product.setSales(product.getSales()-count);
        productMapper.updateById(product);
    }
}