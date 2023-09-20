package com.noname.dto;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SearchProductDTO {
		private String name;
	    private String type;
	    private String shape;
	    private String bulk;
	    private String color;
	    private Integer maxPrice;
	    private Integer minPrice;
	    private String sortBy;
	    private String sortDir;
	    
//	    auctionStart
//	    auctionEnd


	 


}
