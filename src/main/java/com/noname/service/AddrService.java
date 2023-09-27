package com.noname.service;

import com.noname.entity.Address;
import com.noname.entity.Member;

import java.util.List;

public interface AddrService {

    List<Address> findAddrListByMember(Long sid);

    Member findMemberBySid(Long sid);

    void checkAndSaveAddr(Long sid, Address address, Boolean addrCheckbox);

    void deleteAddrById(Long addressId);

    void updateAddr(Long sid, Address address, Boolean addrCheckbox, Long addressId);

    Address findOneAddrBySid(Long sid);
}
