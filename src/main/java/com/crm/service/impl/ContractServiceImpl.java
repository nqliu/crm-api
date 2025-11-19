package com.crm.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.convert.ContractConvert;
import com.crm.entity.*;
import com.crm.enums.ContractStatusEnum;
import com.crm.mapper.ApprovalMapper;
import com.crm.mapper.ContractMapper;
import com.crm.mapper.ContractProductMapper;
import com.crm.mapper.ManagerMapper;
import com.crm.mapper.ProductMapper;
import com.crm.query.ApprovalQuery;
import com.crm.query.ContractQuery;
import com.crm.query.IdQuery;
import com.crm.security.user.SecurityUser;
import com.crm.service.ContractService;
import com.crm.service.EmailService;
import com.crm.utils.DateUtils;
import com.crm.vo.ContractTrendPieVO;
import com.crm.vo.ContractVO;
import com.crm.vo.ProductVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.crm.vo.ProductVO;
import static com.crm.utils.NumberUtils.generateContractNumber;

/**
 * <p>
 * 合同服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    private final ContractMapper contractMapper;
    private final ContractProductMapper contractProductMapper;
    private final ApprovalMapper approvalMapper;
    private final ManagerMapper managerMapper;
    private final EmailService emailService;
    private final ProductMapper productMapper;

    /**
     * 分页查询合同列表
     */
    @Override
    public PageResult<ContractVO> getPage(ContractQuery query) {
        Integer managerId = SecurityUser.getManagerId();
        log.info("查询当前登录员工[{}]的合同列表", managerId);

        Page<ContractVO> page = new Page<>(query.getPage(), query.getLimit());
        MPJLambdaWrapper<Contract> wrapper = new MPJLambdaWrapper<Contract>()
                .selectAll(Contract.class)
                .selectAs(Customer::getName, ContractVO::getCustomerName)
                .leftJoin(Customer.class, Customer::getId, Contract::getCustomerId)
                .eq(Contract::getOwnerId, managerId)
                .eq(Contract::getDeleteFlag, 0)
                .orderByDesc(Contract::getCreateTime);

        // 筛选条件
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like(Contract::getName, query.getName());
        }
        if (query.getCustomerId() != null) {
            wrapper.eq(Contract::getCustomerId, query.getCustomerId());
        }
        if (StringUtils.isNotBlank(query.getNumber())) {
            wrapper.like(Contract::getNumber, query.getNumber());
        }
        // 枚举状态校验
        if (query.getStatus() != null) {
            if (ContractStatusEnum.getByValue(query.getStatus()) == null) {
                throw new ServerException("无效的合同状态");
            }
            wrapper.eq(Contract::getStatus, query.getStatus());
        }

        Page<ContractVO> resultPage = contractMapper.selectJoinPage(page, ContractVO.class, wrapper);

        // 关联产品信息
        resultPage.getRecords().forEach(vo -> {
            List<ContractProduct> products = contractProductMapper.selectList(
                    new LambdaQueryWrapper<ContractProduct>()
                            .eq(ContractProduct::getCId, vo.getId())
            );
            vo.setProducts(ContractConvert.INSTANCE.convertToProductVOList(products));
        });

        return new PageResult<>(resultPage.getRecords(), resultPage.getTotal());
    }

    /**
     * 保存或更新合同
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ContractVO contractVO) throws ServerException{
        boolean isNew = contractVO.getId() == null;

        // 校验合同名称唯一性
        if (isNew && contractMapper.exists(new LambdaQueryWrapper<Contract>()
                .eq(Contract::getName, contractVO.getName())
                .eq(Contract::getDeleteFlag, 0))) {
            throw new ServerException("合同名称已存在");
        }

        // 转换VO为实体
        Contract contract = ContractConvert.INSTANCE.convert(contractVO);
        contract.setCreaterId(SecurityUser.getManagerId());
        contract.setOwnerId(SecurityUser.getManagerId());

        // 设置默认值
        if (contract.getReceivedAmount() == null) {
            contract.setReceivedAmount(BigDecimal.ZERO);
        }
        if (contract.getStatus() == null) {
            contract.setStatus(ContractStatusEnum.INIT.getValue());
        }

        // 新增/更新合同
        if (isNew) {
            contract.setNumber(generateContractNumber());
            contractMapper.insert(contract);
            log.info("新增合同ID：{}", contract.getId());
        } else {
            Contract old = contractMapper.selectById(contractVO.getId());
            if (old == null) {
                throw new ServerException("合同不存在");
            }
            // 审核中不可修改
            if (ContractStatusEnum.UNDER_REVIEW.getValue().equals(old.getStatus())) {
                throw new ServerException("审核中合同无法修改");
            }
            contractMapper.updateById(contract);
        }

        // 处理合同商品关联
        handleContractProducts(contract.getId(), contractVO.getProducts());
    }

    /**
     * 处理合同与商品的关联关系
     */
    private void handleContractProducts(Integer contractId, List<ProductVO> newProductList) {
        if (newProductList == null) return;

        // 查询原有商品关联
        List<ContractProduct> oldProducts = contractProductMapper.selectList(
                new LambdaQueryWrapper<ContractProduct>().eq(ContractProduct::getCId, contractId)
        );

        // 1. 新增商品
        List<ProductVO> added = newProductList.stream()
                .filter(np -> oldProducts.stream().noneMatch(op -> op.getPId().equals(np.getPId())))
                .toList();
        for (ProductVO p : added) {
            Product product = checkProduct(p.getPId(), p.getCount());
            decreaseStock(product, p.getCount());
            ContractProduct cp = builderContractProduct(contractId, product, p.getCount());
            contractProductMapper.insert(cp);
        }

        // 2. 修改商品数量
        List<ProductVO> changed = newProductList.stream()
                .filter(np -> oldProducts.stream()
                        .anyMatch(op -> op.getPId().equals(np.getPId()) && !op.getCount().equals(np.getCount())))
                .toList();
        for (ProductVO p : changed) {
            ContractProduct old = oldProducts.stream()
                    .filter(op -> op.getPId().equals(p.getPId()))
                    .findFirst().orElseThrow();

            Product product = checkProduct(p.getPId(), 0);
            int diff = p.getCount() - old.getCount();

            // 调整库存
            if (diff > 0) {
                decreaseStock(product, diff);
            } else if (diff < 0) {
                increaseStock(product, -diff);
            }

            // 更新关联信息
            old.setCount(p.getCount());
            old.setPrice(product.getPrice());
            old.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(p.getCount())));
            contractProductMapper.updateById(old);
        }

        // 3. 删除商品
        List<ContractProduct> removed = oldProducts.stream()
                .filter(op -> newProductList.stream().noneMatch(np -> np.getPId().equals(op.getPId())))
                .toList();
        for (ContractProduct rm : removed) {
            Product product = productMapper.selectById(rm.getPId());
            if (product != null) {
                increaseStock(product, rm.getCount());
            }
            contractProductMapper.deleteById(rm.getId());
        }
    }

    /**
     * 创建合同商品关联实体
     */
    private ContractProduct builderContractProduct(Integer contractId, Product product, int count) {
        ContractProduct contractProduct = new ContractProduct();
        contractProduct.setCId(contractId);
        contractProduct.setPId(product.getId());
        contractProduct.setPName(product.getName());
        contractProduct.setPrice(product.getPrice());
        contractProduct.setCount(count);
        contractProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(count)));
        return contractProduct;
    }

    /**
     * 检查商品合法性及库存
     */
    private Product checkProduct(Integer productId, int count) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new ServerException("商品不存在");
        }
        if (count > 0 && product.getStock() < count) {
            throw new ServerException("商品库存不足");
        }
        return product;
    }

    /**
     * 增加商品库存
     */
    private void increaseStock(Product product, int count) {
        product.setStock(product.getStock() + count);
        product.setSales(product.getSales() - count);
        productMapper.updateById(product);
    }

    /**
     * 减少商品库存
     */
    private void decreaseStock(Product product, int count) {
        product.setStock(product.getStock() - count);
        product.setSales(product.getSales() + count);
        productMapper.updateById(product);
    }

    /**
     * 获取合同状态分布饼图数据
     */
    @Override
    public List<ContractTrendPieVO> getContractStatusPieData() {
        Integer managerId = SecurityUser.getManagerId();
        List<ContractTrendPieVO> pieData = contractMapper.countByStatus(managerId);

        // 计算占比
        int total = pieData.stream()
                .mapToInt(ContractTrendPieVO::getCount)
                .sum();
        pieData.forEach(item -> {
            item.setProportion(total > 0 ? (double) item.getCount() / total * 100 : 0);
        });

        return pieData;
    }

    /**
     * 发起合同审核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startApproval(IdQuery idQuery) {
        Contract contract = contractMapper.selectById(idQuery.getId());
        if (contract == null) {
            throw new ServerException("合同不存在");
        }
        // 仅初始化状态可发起审核
        if (!ContractStatusEnum.INIT.getValue().equals(contract.getStatus())) {
            throw new ServerException("只有初始化状态的合同可发起审核");
        }

        // 更新为审核中状态
        contract.setStatus(ContractStatusEnum.UNDER_REVIEW.getValue());
        contract.setUpdateTime(LocalDateTime.now());
        contractMapper.updateById(contract);
    }

    /**
     * 审核合同（通过/拒绝）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvalContract(ApprovalQuery query) {
        // 校验审核内容
        if (StringUtils.isBlank(query.getComment())) {
            throw new ServerException("请填写审核意见");
        }

        // 查询合同
        Contract contract = contractMapper.selectById(query.getId());
        if (contract == null) {
            throw new ServerException("合同不存在");
        }
        // 仅审核中状态可操作
        if (!ContractStatusEnum.UNDER_REVIEW.getValue().equals(contract.getStatus())) {
            throw new ServerException("合同未在审核状态");
        }

        // 保存审核记录
        Approval approval = new Approval();
        approval.setStatus(query.getType());
        approval.setCreaterId(SecurityUser.getManagerId());
        approval.setContractId(query.getId());
        approval.setComment(query.getComment());
        approval.setCreateTime(LocalDateTime.now());
        approvalMapper.insert(approval);

        // 更新合同状态
        Integer targetStatus = query.getType() == 0
                ? ContractStatusEnum.APPROVED.getValue()
                : ContractStatusEnum.REJECTED.getValue();
        contract.setStatus(targetStatus);
        contract.setUpdateTime(LocalDateTime.now());
        contractMapper.updateById(contract);

        // 发送审核结果邮件
        sendApprovalEmail(contract, query.getType() == 0, query.getComment());
    }

    /**
     * 发送审核结果邮件
     */
    private void sendApprovalEmail(Contract contract, boolean isApproved, String comment) {
        try {
            // 查询合同创建人（销售）
            Manager seller = managerMapper.selectById(contract.getCreaterId());
            if (seller == null || StringUtils.isBlank(seller.getEmail())) {
                log.warn("合同创建人邮箱不存在，无法发送邮件。合同ID: {}", contract.getId());
                return;
            }

            // 构建邮件内容
            String subject = isApproved ? "合同审核通过通知" : "合同审核未通过通知";
            String content = String.format(
                    "您的合同《%s》已%s审核！\n审核意见：%s\n合同编号：%s\n审核时间：%s",
                    contract.getName(),
                    isApproved ? "通过" : "未通过",
                    comment,
                    contract.getNumber(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );

            // 发送邮件
            emailService.sendSimpleMail(seller.getEmail(), subject, content);
        } catch (Exception e) {
            log.error("发送审核邮件失败", e);
            // 邮件发送失败不影响主流程
        }
    }

    /**
     * 统计当日审核总数（通过+拒绝）
     */
    @Override
    public Integer countTodayApprovalTotal() {
        String today = LocalDate.now().toString();
        Integer managerId = SecurityUser.getManagerId();

        int approved = contractMapper.countByStatusAndDate(
                managerId, today, ContractStatusEnum.APPROVED.getValue()
        );
        int rejected = contractMapper.countByStatusAndDate(
                managerId, today, ContractStatusEnum.REJECTED.getValue()
        );

        return approved + rejected;
    }
}