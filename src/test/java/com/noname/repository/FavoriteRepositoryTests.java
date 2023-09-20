package com.noname.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.noname.entity.Favorite;
import com.noname.entity.Member;
import com.noname.entity.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FavoriteRepositoryTests {
	
	
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	ProductRepository productRepository;
	
//	
//	@Test
//	@Transactional
//	public void existsByProductAndMemberTest() {
//		
//		Member member = memberRepository.findById(1L).get();
//		
//		Product product = productRepository.findById(1L).get();
//		
//		
//		boolean isFalse = favoriteRepository.existsByProductAndMember(product, member);
//		log.info("is false : {}" , isFalse);
//		
//		
//		Favorite favorite = new Favorite();
//		favorite.setMember(member);
//		favorite.setProduct(product);
//		favoriteRepository.save(favorite);
//		
//		boolean isTrue = favoriteRepository.existsByProductAndMember(product, member);
//		log.info("is true : {}" , isTrue);
//		
//	}
//	
//	
//	@Test
//	@Transactional
//	public void findByProductAndMemberTest() {
//		
//		Member member = memberRepository.findById(1L).get();
//		
//		Product product = productRepository.findById(1L).get();
//		
//		
//		Optional<Favorite> optional1 = favoriteRepository.findByProductAndMember(product, member);
//		log.info("is false : {}" , optional1.isPresent());
//		
//		
//		Favorite favorite = new Favorite();
//		favorite.setMember(member);
//		favorite.setProduct(product);
//		favoriteRepository.save(favorite);
//		
//		Optional<Favorite> optional2 = favoriteRepository.findByProductAndMember(product, member);
//		log.info("is ture : {}" , optional2.isPresent());
//		
//	}
//	
//	@Test
//	@Transactional
//	public void removeFavariteTest() {
//		
//		Integer count1 = (int) favoriteRepository.count();
//		
//		Member member = memberRepository.findById(1L).get();
//		
//		Product product = productRepository.findById(1L).get();
//		Favorite favorite = new Favorite();
//		favorite.setMember(member);
//		favorite.setProduct(product);
//		favoriteRepository.save(favorite);
//		
//		Optional<Favorite> optional = favoriteRepository.findByProductAndMember(product, member);
//	
//		Favorite found = optional.get();
//		
//		
//		favoriteRepository.delete(found);
//		Integer count2 = (int) favoriteRepository.count();
//		Assertions.assertThat(count1).isEqualTo(count2);
//		
//	}
	
//	@Test
//	@Transactional
//	public void findFavoritesWithProductsByMember() {
//		Member member = memberRepository.findById(1L).get();
//		for(int i=1; i < 10; i++) {
//			
//			Product product = productRepository.findById(1L + i).get();
//			Favorite favorite = new Favorite();
//			favorite.setMember(member);
//			favorite.setProduct(product);
//			favoriteRepository.save(favorite);
//			
//		}
//
//		List<Favorite> list = favoriteRepository.findWithProductsByMember(member);
//		log.info("====================================");
//		list.forEach(favorite -> log.info(favorite.toString()));
//		log.info("====================================");
//		log.info(list.get(0).getProduct().toString());
//		
//	}
	
	@Test
	@Transactional
	public void findByMember() {
		
//		Member member = memberRepository.findById(1L).get();
//		for(int i=1; i < 10; i++) {
//			
//			Product product = productRepository.findById(1L + i).get();
//			Favorite favorite = new Favorite();
//			favorite.setMember(member);
//			favorite.setProduct(product);
//			favoriteRepository.save(favorite);
//			
//		}
		Member member = memberRepository.findById(1L).get();
		favoriteRepository.count();
		List<Product> list = favoriteRepository.findFavoriteByMember(member);
		
//		List<Favorite> list = favoriteRepository.findByMember(member);
		log.info("==================================== {}", favoriteRepository.count());
		list.forEach(favorite -> log.info(favorite.toString()));
		log.info("====================================");
//		log.info(list.get(0).getProduct().toString());
		
	}
	
	
	
	
	
	
	

}
