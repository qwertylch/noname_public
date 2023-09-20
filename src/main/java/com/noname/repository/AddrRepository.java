package com.noname.repository;

import com.noname.entity.Address;
import com.noname.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddrRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByMember(Member member);


}
