package com.noname.dto;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.noname.entity.Favorite;
import com.noname.entity.Product;
import com.noname.entity.ProductImage;
import com.noname.enums.AuctionStatus;
import com.noname.util.DateConverter;
import com.noname.util.NumberConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFavoriteDTO {
	
	private Long productId;
	private String productName;
	private String endDate;
	private String startDate;
	private Long diffDate;
	private String currentPrice;
	private String productImage;
	private String auctionStatus;
	private boolean favorite;
	
	
	 public static ResponseFavoriteDTO convertToDTO(Product product, Integer currentPrice){
		 return ResponseFavoriteDTO.builder()
				 .productId(product.getProductId())
				 .productName(product.getName())
				 .currentPrice(NumberConverter.formatNumberWithCommas(currentPrice))
				 .endDate(DateConverter.formatToMMDDAHHMM(product.getProductAuction().getEndTime()))
				 .startDate(DateConverter.formatToMMDDAHHMM(product.getProductAuction().getStartTime()))
				 .diffDate(DateConverter.timeDifferenceToMillis(product.getProductAuction().getStartTime()))
				 .productImage(convertProductImage(product.getProductImages().get(0)))
				 .auctionStatus(AuctionStatus.valueOf(product.getProductAuction().getStatus().name()).name())
				 .favorite(true)
				 .build();			
				
	 }
	 
    private static String convertProductImage(ProductImage image) {
    	  try {
              return URLEncoder.encode(image.getPath() + File.separator + "th_" +image.getIdentifier() + image.getName(), StandardCharsets.UTF_8);
          } catch (Exception e) {
              throw new RuntimeException("Error encoding image URL", e);

          } 
    }
    


	
	
	
	
	
	
}
