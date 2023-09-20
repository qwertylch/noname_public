package com.noname.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.enums.AuctionStatus;
import com.noname.enums.ProductStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRepositoryTests {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Test
	public void getMemberWithProductsAndAuctionAndImage() {
		Long memberId = 1L;
		
		Member member = memberRepository.findMemberWithProductsAndAuction(memberId);
		member.getProducts().stream().forEach(p -> log.info("Product: {}    productAuction: {}", p.toString(), p.getProductAuction().toString() ));
		 
	
		
		 
    }
	

}
