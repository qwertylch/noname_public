package com.noname.dto;


import com.noname.entity.Bidding;
import com.noname.util.DateConverter;
import com.noname.util.NumberConverter;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseBidDTO {

    private String memberName;
    private String bidPrice;
    private String bidDate;
    
    public static ResponseBidDTO convertToDTO(Bidding bidding){
		
    	return ResponseBidDTO.builder()
    			.memberName(bidding.getMember().getName())
    			.bidPrice(NumberConverter.formatNumberWithCommas(bidding.getBidPrice()))
    			.bidDate(DateConverter.formatToYYYYMMDD(bidding.getCreateDate()))
    			.build();
    	
    }


    

    

}
