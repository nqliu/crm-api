package com.crm.convert;

import com.crm.entity.SysMenu;
import com.crm.vo.SysMenuVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-12T14:45:13+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
public class SysMenuConvertImpl implements SysMenuConvert {

    @Override
    public SysMenu convert(SysMenuVO vo) {
        if ( vo == null ) {
            return null;
        }

        SysMenu sysMenu = new SysMenu();

        sysMenu.setId( vo.getId() );
        sysMenu.setParentId( vo.getParentId() );
        sysMenu.setName( vo.getName() );
        sysMenu.setTitle( vo.getTitle() );
        sysMenu.setPath( vo.getPath() );
        sysMenu.setComponent( vo.getComponent() );
        sysMenu.setType( vo.getType() );
        sysMenu.setOpenType( vo.getOpenType() );
        sysMenu.setUrl( vo.getUrl() );
        sysMenu.setIcon( vo.getIcon() );
        sysMenu.setAuth( vo.getAuth() );
        sysMenu.setKeepAlive( vo.getKeepAlive() );
        sysMenu.setSort( vo.getSort() );

        return sysMenu;
    }

    @Override
    public SysMenuVO convert(SysMenu entity) {
        if ( entity == null ) {
            return null;
        }

        SysMenuVO sysMenuVO = new SysMenuVO();

        sysMenuVO.setId( entity.getId() );
        sysMenuVO.setParentId( entity.getParentId() );
        sysMenuVO.setName( entity.getName() );
        sysMenuVO.setTitle( entity.getTitle() );
        sysMenuVO.setPath( entity.getPath() );
        sysMenuVO.setComponent( entity.getComponent() );
        sysMenuVO.setType( entity.getType() );
        sysMenuVO.setOpenType( entity.getOpenType() );
        sysMenuVO.setUrl( entity.getUrl() );
        sysMenuVO.setIcon( entity.getIcon() );
        sysMenuVO.setAuth( entity.getAuth() );
        sysMenuVO.setKeepAlive( entity.getKeepAlive() );
        sysMenuVO.setSort( entity.getSort() );

        return sysMenuVO;
    }

    @Override
    public List<SysMenuVO> convertList(List<SysMenu> list) {
        if ( list == null ) {
            return null;
        }

        List<SysMenuVO> list1 = new ArrayList<SysMenuVO>( list.size() );
        for ( SysMenu sysMenu : list ) {
            list1.add( convert( sysMenu ) );
        }

        return list1;
    }
}
