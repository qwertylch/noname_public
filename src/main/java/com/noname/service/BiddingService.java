package com.noname.service;

import com.noname.dto.ResponseBidDTO;

public interface BiddingService {
	
	public ResponseBidDTO saveBidding(Long ProductId, Long MemberId, Integer bidPrice);
	
	public boolean isMaxBidPrice(Integer bidPrice, Long productId);
	
	public boolean isMaxBidMember(Long memberId, Long productId);
	
}
