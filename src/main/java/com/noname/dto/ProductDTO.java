package com.noname.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString 
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	   private String path;
	    private String identifier;
	    private String name;
	   
	    // Product
	    private Long productId;
	    private String type;
	    private String shape;
	    private String color;
	    // bulk 로 넘길값
	    private String width;
	    private String depth;
	    private String height;

	    private String mnfctDate;
	    private String explanation;

	    // ProductImage
	    private List<String> images;
	    private String productName;

	    // ProductAuction
	    private String startPrice;

	    // ProductAuction
	    private String startTime;
	    private String endTime;

}
