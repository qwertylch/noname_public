package com.noname.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAuctionDTO {
	
	
	private String memberName;
	private String email;
	private Long imageId;
	private String image;
	
	
	private Integer pendingCount;
	private Integer inBidCount;
	private Integer endCount; 
	
	
}
