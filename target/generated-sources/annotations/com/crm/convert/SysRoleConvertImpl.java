package com.crm.convert;

import com.crm.entity.SysRole;
import com.crm.vo.SysRoleVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-12T09:58:21+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
public class SysRoleConvertImpl implements SysRoleConvert {

    @Override
    public SysRoleVO convert(SysRole entity) {
        if ( entity == null ) {
            return null;
        }

        SysRoleVO sysRoleVO = new SysRoleVO();

        sysRoleVO.setId( entity.getId() );
        sysRoleVO.setName( entity.getName() );
        sysRoleVO.setRemark( entity.getRemark() );
        sysRoleVO.setCreateTime( entity.getCreateTime() );

        return sysRoleVO;
    }

    @Override
    public SysRole convert(SysRoleVO vo) {
        if ( vo == null ) {
            return null;
        }

        SysRole sysRole = new SysRole();

        sysRole.setId( vo.getId() );
        sysRole.setName( vo.getName() );
        sysRole.setRemark( vo.getRemark() );
        sysRole.setCreateTime( vo.getCreateTime() );

        return sysRole;
    }

    @Override
    public List<SysRoleVO> convertList(List<SysRole> list) {
        if ( list == null ) {
            return null;
        }

        List<SysRoleVO> list1 = new ArrayList<SysRoleVO>( list.size() );
        for ( SysRole sysRole : list ) {
            list1.add( convert( sysRole ) );
        }

        return list1;
    }
}
