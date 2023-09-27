package com.noname.wsauction;

public class Auction {
	
	private long productId;
	private Long memberId;
	private Integer bidPrice;
	
	public Auction() {
		
	}
	
	public Auction(long productId, Long memberId, Integer bidPrice) {
	
		this.productId = productId;
		this.memberId = memberId;
		this.bidPrice = bidPrice;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public Integer getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(Integer bidPrice) {
		this.bidPrice = bidPrice;
	}

}
