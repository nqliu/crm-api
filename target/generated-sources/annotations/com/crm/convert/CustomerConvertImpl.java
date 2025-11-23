package com.crm.convert;

import com.crm.entity.Customer;
import com.crm.entity.Lead;
import com.crm.vo.CustomerVO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T11:50:31+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
public class CustomerConvertImpl implements CustomerConvert {

    @Override
    public Customer convert(CustomerVO customerVO) {
        if ( customerVO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerVO.getId() );
        customer.setName( customerVO.getName() );
        customer.setPhone( customerVO.getPhone() );
        customer.setEmail( customerVO.getEmail() );
        customer.setLevel( customerVO.getLevel() );
        customer.setSource( customerVO.getSource() );
        customer.setAddress( customerVO.getAddress() );
        customer.setFollowStatus( customerVO.getFollowStatus() );
        customer.setNextFollowStatus( customerVO.getNextFollowStatus() );
        customer.setRemark( customerVO.getRemark() );
        customer.setCreaterId( customerVO.getCreaterId() );
        customer.setIsPublic( customerVO.getIsPublic() );
        customer.setOwnerId( customerVO.getOwnerId() );
        customer.setIsKeyDecisionMaker( customerVO.getIsKeyDecisionMaker() );
        customer.setGender( customerVO.getGender() );
        customer.setDealCount( customerVO.getDealCount() );
        customer.setCreateTime( customerVO.getCreateTime() );

        return customer;
    }

    @Override
    public Customer leadConvert(Lead lead) {
        if ( lead == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( lead.getId() );
        customer.setName( lead.getName() );
        customer.setPhone( lead.getPhone() );
        customer.setEmail( lead.getEmail() );
        customer.setLevel( lead.getLevel() );
        customer.setSource( lead.getSource() );
        customer.setAddress( lead.getAddress() );
        customer.setFollowStatus( lead.getFollowStatus() );
        customer.setNextFollowStatus( lead.getNextFollowStatus() );
        customer.setRemark( lead.getRemark() );
        customer.setOwnerId( lead.getOwnerId() );
        customer.setDeleteFlag( lead.getDeleteFlag() );
        customer.setCreateTime( lead.getCreateTime() );
        customer.setUpdateTime( lead.getUpdateTime() );

        return customer;
    }
}
