package com.noname.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noname.dto.ResponseFavoriteDTO;
import com.noname.entity.Favorite;
import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.repository.BiddingRepository;
import com.noname.repository.FavoriteRepository;
import com.noname.repository.MemberRepository;
import com.noname.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService{
	

	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final FavoriteRepository favoriteRepository;
	private final BiddingRepository biddingRepository;
	
	
	@Autowired
	public FavoriteServiceImpl(FavoriteRepository favoriteRepository, MemberRepository memberRepository, ProductRepository productRepository, BiddingRepository biddingRepository) {
		this.memberRepository = memberRepository;
		this.productRepository = productRepository;
		this.favoriteRepository = favoriteRepository;
		this.biddingRepository = biddingRepository;
		
	}
	
	public List<ResponseFavoriteDTO> getFavorite(Long memberId) {
		Optional<Member> member = memberRepository.findById(memberId);
		requireExists(member);
		return favoriteRepository.findFavoriteByMember(member.get())
								.stream().map(f-> ResponseFavoriteDTO.convertToDTO(f, biddingRepository.findMaxPriceByProduct(f).orElse(0)))
								.collect(Collectors.toList());

	
		 
	}
	
	@Override
    public boolean isFavorite(Long productId, Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Product> product = productRepository.findById(productId);
        if (requireExists(member) && requireExists(product)) {
            return favoriteRepository.existsByProductAndMember(product.get(), member.get());
        }
        return false;
    }
    
	@Override
    public boolean addFavorite(Long productId, Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Product> product = productRepository.findById(productId);

        if (requireExists(member) && requireExists(product)) {
        	
        	Favorite favorite = new Favorite();
        	favorite.setMember(member.get());
        	favorite.setProduct(product.get());
        	Favorite saved = favoriteRepository.save(favorite);
        	
        
            return favoriteRepository.findById(saved.getFavoriteId()).isPresent();
        }
        return false;
    }
    
	@Override
    public boolean removeFavorite(Long productId, Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Product> product = productRepository.findById(productId);

        if (requireExists(member) && requireExists(product)) {
        	favoriteRepository.deleteByProductAndMember(product.get(), member.get());
        	
            return !favoriteRepository.existsByProductAndMember(product.get(), member.get());
        }
        return false;
    }
    
    

    private boolean requireExists(Optional<?> optional) {
        if (!optional.isPresent()) {
            String entityName = optional.map(Object::getClass)
                                        .map(Class::getSimpleName)
                                        .orElse("Entity"); 
            throw new EntityNotFoundException(entityName + " not found");
        }
        return true;
    }
	
	
	
	
	
	

}
