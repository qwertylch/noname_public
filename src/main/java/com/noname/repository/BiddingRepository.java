package com.noname.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.noname.dto.BiddingStatusDTO;
import com.noname.dto.MaxBidPrice;
import com.noname.entity.Bidding;
import com.noname.entity.Member;
import com.noname.entity.Product;

public interface BiddingRepository extends JpaRepository<Bidding, Long>{
	

    
	 @Query("SELECT new com.noname.dto.BiddingStatusDTO(b, pa.status) " +
	            "FROM Bidding b " +
	            "LEFT JOIN b.product p " +
	            "LEFT JOIN p.productAuction pa " +
	            "WHERE (p.member.memberId = :sid OR b.member.memberId = :sid) " +
	            "AND b.bidPrice = (SELECT MAX(b2.bidPrice) FROM Bidding b2 WHERE b2.product = p)")
	    List<BiddingStatusDTO> findMaxBiddingWithAuctionStatusForEachProductByMemberSid(@Param("sid") Long sid);

    @Query("SELECT b from Bidding b WHERE b.bidId = :bidId")
    public Bidding findByBidId(@Param("bidId") Long bidId);

    @Query("SELECT MAX(b.bidPrice) FROM Bidding b where b.product.productId = :productId")
    public int findMaxPriceByProductId(@Param("productId") Long productId);
    
    @Query("SELECT MAX(b.bidPrice) FROM Bidding b WHERE b.product = :product")
    public Optional<Integer> findMaxPriceByProduct(@Param("product") Product product);
    
    public Optional<Bidding> findTopByOrderByBidPriceDesc();
    
    @Query("SELECT NEW com.noname.dto.MaxBidPrice(MAX(b.bidPrice), b.member.memberId) "
    		+ "FROM Bidding b WHERE b.product = :product "
    		+ "GROUP BY b.member.memberId "
    		+ "ORDER BY MAX(b.bidPrice) DESC")
    public List<MaxBidPrice> findMaxBidMemberByProduct(@Param("product") Product product);

}
