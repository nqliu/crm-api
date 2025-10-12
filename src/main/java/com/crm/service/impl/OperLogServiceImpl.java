package com.crm.service.impl;

import com.crm.entity.OperLog;
import com.crm.mapper.OperLogMapper;
import com.crm.service.OperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

}
