package com.noname.dto;

import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;
import com.noname.util.DateConverter;
import com.noname.util.NumberConverter;

import lombok.*;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Auction {
	private Long productId;
	private String productName;
	private String productImage;
	private String memberName;
	private String originalPrice;
	private String currentPrice;
	private String startDate;
	private String endDate;
	private String auctionStatus;
	private Long diffDate;
	private boolean favorite;

	public Auction(Long productId
			,String productName
			, Member member
			,Integer currentPrice
			, ProductImage productImage
			, ProductAuction productAuction
			, Long diffDate
			,boolean favorite) {
		this.productId = productId;
		this.productName = productName;
		this.memberName = member.getName();
		this.originalPrice = NumberConverter.formatNumberWithCommas(productAuction.getStartPrice());
		this.currentPrice = NumberConverter.formatNumberWithCommas(currentPrice);
		this.startDate = DateConverter.formatToMMDDAHHMM(productAuction.getStartTime());
		this.endDate = DateConverter.formatToMMDDAHHMM(productAuction.getEndTime());
		this.productImage =  URLEncoder.encode(productImage.getPath() + File.separator + "th_" + productImage.getIdentifier() + productImage.getName(), StandardCharsets.UTF_8);
		this.auctionStatus = productAuction.getStatus().name();
		this.diffDate = diffDate;
		this.favorite = favorite;
	}
}

