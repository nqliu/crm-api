package com.crm.convert;

import com.crm.entity.SysManager;
import com.crm.security.user.ManagerDetail;
import com.crm.vo.SysManagerVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-08T09:02:00+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
public class SysManagerConvertImpl implements SysManagerConvert {

    @Override
    public SysManager convert(SysManagerVO vo) {
        if ( vo == null ) {
            return null;
        }

        SysManager sysManager = new SysManager();

        sysManager.setId( vo.getId() );
        sysManager.setAccount( vo.getAccount() );
        sysManager.setNickname( vo.getNickname() );
        sysManager.setPassword( vo.getPassword() );
        sysManager.setStatus( vo.getStatus() );
        sysManager.setCreateTime( vo.getCreateTime() );

        return sysManager;
    }

    @Override
    public ManagerDetail convertDetail(SysManager entity) {
        if ( entity == null ) {
            return null;
        }

        ManagerDetail managerDetail = new ManagerDetail();

        managerDetail.setId( entity.getId() );
        managerDetail.setAccount( entity.getAccount() );
        managerDetail.setPassword( entity.getPassword() );
        managerDetail.setStatus( entity.getStatus() );

        return managerDetail;
    }

    @Override
    public List<SysManagerVO> convertList(List<SysManager> list) {
        if ( list == null ) {
            return null;
        }

        List<SysManagerVO> list1 = new ArrayList<SysManagerVO>( list.size() );
        for ( SysManager sysManager : list ) {
            list1.add( sysManagerToSysManagerVO( sysManager ) );
        }

        return list1;
    }

    protected SysManagerVO sysManagerToSysManagerVO(SysManager sysManager) {
        if ( sysManager == null ) {
            return null;
        }

        SysManagerVO sysManagerVO = new SysManagerVO();

        sysManagerVO.setId( sysManager.getId() );
        sysManagerVO.setAccount( sysManager.getAccount() );
        sysManagerVO.setNickname( sysManager.getNickname() );
        sysManagerVO.setStatus( sysManager.getStatus() );
        sysManagerVO.setPassword( sysManager.getPassword() );
        sysManagerVO.setCreateTime( sysManager.getCreateTime() );

        return sysManagerVO;
    }
}
