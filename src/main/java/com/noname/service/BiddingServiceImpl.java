package com.noname.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.noname.dto.MaxBidPrice;
import com.noname.dto.ResponseBidDTO;
import com.noname.entity.Bidding;
import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.repository.BiddingRepository;
import com.noname.repository.MemberRepository;
import com.noname.repository.ProductRepository;


@Service
public class BiddingServiceImpl implements BiddingService{




	private final BiddingRepository biddingRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	
	public BiddingServiceImpl(BiddingRepository biddingRepository, MemberRepository memberRepository,
			ProductRepository productRepository) {
		super();
		this.biddingRepository = biddingRepository;
		this.memberRepository = memberRepository;
		this.productRepository = productRepository;
	}
	

	@Override
	public ResponseBidDTO saveBidding(Long productId, Long memberId, Integer bidPrice) {
		Optional<Member> selectedMember = memberRepository.findById(memberId);
		Optional<Product> selectedProduct = productRepository.findById(productId);

		requireExists(selectedMember);
		requireExists(selectedProduct);

		Product product = selectedProduct.get();
		Member member = selectedMember.get();

		Bidding bidding = new Bidding();
		bidding.setBidPrice(bidPrice);
		bidding.setMember(member);
		bidding.setProduct(product);
		Bidding savedBidding = biddingRepository.save(bidding);

		return ResponseBidDTO.convertToDTO(savedBidding);
	}
	
	
	@Override
	public boolean isMaxBidPrice(Integer bidPrice, Long productId) {
		Optional<Product> selectedProduct = productRepository.findById(productId);
		requireExists(selectedProduct);
		boolean result = true;
		
		Optional<Integer> maxPrice = biddingRepository.findMaxPriceByProduct(selectedProduct.get());
		if(maxPrice.isPresent()) {
			if(bidPrice < maxPrice.get()) {
				result = false;
			}
		}
		
		return result;
		
	}
	
	@Override
	public boolean isMaxBidMember(Long memberId, Long productId) {
		Optional<Product> selectedProduct = productRepository.findById(productId);
		requireExists(selectedProduct);
		boolean result = false;
		
		List<MaxBidPrice> maxBid = biddingRepository.findMaxBidMemberByProduct(selectedProduct.get());
		if(!maxBid.isEmpty()) {
			if(maxBid.get(0).getMemberId() == memberId) {
				result = true;
			};
		}
		
		return result;
	}
	
	
	

    private void requireExists(Optional<?> optional) {
        if (!optional.isPresent()) {
            String entityName = optional.map(Object::getClass)
                                        .map(Class::getSimpleName)
                                        .orElse("Entity"); 
            throw new EntityNotFoundException(entityName + " not found");
        }
    }

}
