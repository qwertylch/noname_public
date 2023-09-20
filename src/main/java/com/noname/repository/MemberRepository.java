package com.noname.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.noname.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
    @Query(value = "SELECT m FROM Member m LEFT JOIN FETCH m.products p LEFT JOIN FETCH p.productAuction pa LEFT JOIN FETCH m.images WHERE m.memberId = :memberId")
    public Member findMemberWithProductsAndAuction(@Param("memberId") Long memberId);
	
    public Member findByEmail(String email);
    
}
