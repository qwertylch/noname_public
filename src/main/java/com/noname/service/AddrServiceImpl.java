package com.noname.service;

import com.noname.entity.Address;
import com.noname.entity.Member;
import com.noname.enums.AddressType;
import com.noname.repository.AddrRepository;
import com.noname.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AddrServiceImpl implements AddrService {

    private final AddrRepository addrRepository;
    private final MemberRepository memberRepository;

    @Override
    public Member findMemberBySid(Long sid) {
        return memberRepository.findById(sid).get();
    }

    public List<Address> findAddrListByMember(Long sid) {
        Member member1 = findMemberBySid(sid);
        return member1.getAddresses();
    }

    public Optional<Address> findAddrByAddrId(Long addressId) {
        return addrRepository.findById(addressId);
    }

    public void checkAddrCheckbox(Long sid,Address address, Boolean addrCheckbox) {
        List<Address> addresses = findAddrListByMember(sid);
        if (addrCheckbox == null) {
            addrCheckbox = false;
            address.setStatus(AddressType.DISABLED);
        } else if (addrCheckbox) {
            for (Address memberAddress : addresses) {
                if (memberAddress.getStatus() == AddressType.ACTIVE) {
                    memberAddress.setStatus(AddressType.DISABLED);
                }
            }
            address.setStatus(AddressType.ACTIVE);
        }
    }

    @Override
    @Transactional
    public void checkAndSaveAddr(Long sid, Address address, Boolean addrCheckbox) {
        Member foundMember = findMemberBySid(sid);
        address.setMember(foundMember);
        Long aId = address.getAddressId();
        addrRepository.save(address);
        //기존 기본 배송지 상태값 해제
        checkAddrCheckbox(sid, address, addrCheckbox);

    }

    @Override
    @Transactional
    public void deleteAddrById(Long addressId) {
        Optional<Address> delAddress = addrRepository.findById(addressId);
        if (delAddress.get().getStatus() != AddressType.ACTIVE) {
            delAddress.ifPresent(address -> address.setStatus(AddressType.DELETED));
        }
    }

    @Override
    @Transactional
    public void updateAddr(Long sid, Address address, Boolean addrCheckbox, Long addressId) {
        Member foundMember = findMemberBySid(sid);
        address.setMember(foundMember);
        Optional<Address> updateAddr = addrRepository.findById(addressId);
        log.info("====updateAddr : {}", updateAddr);
        address.setAddressId(addressId);
        if (updateAddr.get().getStatus().equals(AddressType.ACTIVE)) {
            log.info("====updateAddr Status : {}", updateAddr.get().getStatus());
            address.setStatus(AddressType.ACTIVE);
            addrRepository.save(address);
        }else {
            checkAddrCheckbox(sid, address, addrCheckbox);
        }
        addrRepository.save(address);
    }

    @Override
    public Address findOneAddrBySid(Long sid) {
        List<Address> addressList = findAddrListByMember(sid);
        for (Address address : addressList) {
            if ("ACTIVE".equals(address.getStatus().toString())) {
                return address;
            }
        }
        return null;
    }


}