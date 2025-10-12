package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.entity.Department;
import com.crm.entity.SysManager;
import com.crm.mapper.DepartmentMapper;
import com.crm.mapper.SysManagerMapper;
import com.crm.query.DepartmentQuery;
import com.crm.query.IdQuery;
import com.crm.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    private final SysManagerMapper sysManagerMapper;
    @Override
    public PageResult<Department> getPage(DepartmentQuery query) {
        //1.构建条件查询wrapper
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(Department::getName, query.getName());
        }
        List<Department> departments = baseMapper.selectList(wrapper);
        if (departments.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0);
        }


        Integer minLevel = departments.stream().map(Department::getLevel).min(Integer::compareTo).orElse(0);

        List<Department> topDepartment = departments.stream().filter(department -> department.getLevel().equals(minLevel)).toList();

        int total = topDepartment.size();
        int formIndex = (query.getPage() - 1) * query.getLimit();
        int toIndex = Math.min(formIndex + query.getLimit(), total);
        if (formIndex >= total) {
            return new PageResult<>(Collections.emptyList(), total);
        }

        List<Department> result = topDepartment.subList(formIndex, toIndex);
        result.forEach(item -> getChildList(item,departments));
        return new PageResult<>(result,total);
    }
    private Department getChildList(Department department, List<Department> list) {
        list.forEach(item -> {
            if (department.getId().equals(item.getParentId())) {
                department.getChildren().add(getChildList(item, list));
            }
        });
        return department;
    }
    @Override
    public List<Department> getList() {
//        1、查询父级部门列表,如果列表为空，返回空集合
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<Department>();
        wrapper.eq(Department::getParentId, 0);
        List<Department> parentDepartments = baseMapper.selectList(wrapper);
        if (parentDepartments.isEmpty()) {
            return new ArrayList<>();
        }
        wrapper.clear();
//        2、查询子部门列表，用于父子关系的递归
        wrapper.ne(Department::getParentId, 0);
        List<Department> childDepartments = baseMapper.selectList(wrapper);
        if (childDepartments.isEmpty()) {
            return parentDepartments;
        }
//        3、递归构建父子关系
        parentDepartments.forEach(item -> {
            getChildList(item, childDepartments);
        });
        return parentDepartments;
    }
    @Override
    public void saveOrEditDepartment(Department department) {
//        1、查询新增/修改的部门名称是不是已经存在了，如果存在直接抛出异常
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<Department>().eq(Department::getName, department.getName());
        if (department.getId() == null) {
            wrapper.eq(Department::getParentId, department.getParentId());
            List<Department> departments = baseMapper.selectList(wrapper);
            if (!departments.isEmpty()) {
                throw new ServerException("部门名称已存在");
            }
//            2、新增是判断是否有上级，如果有上级要获取上级部门的部门信息，存储父子关系
            if (department.getParentId() != null) {
                Department parentDepart = baseMapper.selectById(department.getParentId());
                if (parentDepart == null) {
                    throw new ServerException("上级部门不存在");
                } else {
                    if (parentDepart.getParentIds() == null || parentDepart.getParentIds().isEmpty()) {
                        department.setParentIds(parentDepart.getId().toString());
                    } else {
                        department.setParentIds(parentDepart.getParentIds() + "," + parentDepart.getId());
                    }
                    department.setLevel(parentDepart.getLevel() + 1);
                }
            }
            baseMapper.insert(department);
        } else {
            Department aDepartment = baseMapper.selectById(department.getId());
            if (aDepartment == null) {
                throw new ServerException("部门不存在");
            }
            wrapper.ne(Department::getId, department.getId()).eq(Department::getParentId, department.getParentId());
            List<Department> departments = baseMapper.selectList(wrapper);
            if (!departments.isEmpty()) {
                throw new ServerException("部门名称已存在");
            }
//            3、修改部门存在上级部门且上级部门的信息发生了变化时，要修改关联的父子关系关联的id
            if (department.getParentId() != 0 && !Objects.equals(department.getParentId(), aDepartment.getParentId())) {
                Department parentDepart = baseMapper.selectById(department.getParentId());
                String parentIds = aDepartment.getParentIds();
                String newParentIds = "";
                if (parentDepart == null) {
                    throw new ServerException("上级部门不存在");
                }
                if (parentDepart.getParentIds() == null || parentDepart.getParentIds().isEmpty()) {
                    newParentIds = parentDepart.getId().toString();

                } else {
                    newParentIds = parentDepart.getParentIds() + "," + parentDepart.getId();
                }
                department.setLevel(parentDepart.getLevel() + 1);
                department.setParentIds(newParentIds);
                String finalNewParentIds = newParentIds;
                baseMapper.selectList(new LambdaQueryWrapper<Department>().eq(Department::getParentIds, parentIds)).forEach(item -> {
                    item.setParentIds(item.getParentIds().replace(parentIds, finalNewParentIds));
                    baseMapper.updateById(item);
                });
            }
            baseMapper.updateById(department);
        }

    }
    @Override
    public void removeDepartment(IdQuery query) {
//        List<SysManager> sysManagers = sysManagerMapper.selectList(new LambdaQueryWrapper<SysManager>().eq(SysManager::getDepartmentId, query.getId()));
//        if (!sysManagers.isEmpty()) {
//            throw new ServerException("部门下有管理员,请解绑后再删除");
//        }
        // 删除该部门以及子部门
        List<Department> departments = baseMapper.selectList(new LambdaQueryWrapper<Department>().like(Department::getParentIds, query.getId()).or().eq(Department::getId, query.getId()));
        removeBatchByIds(departments);
    }

}