package com.noname.dto;


import com.noname.entity.Bidding;
import com.noname.entity.MemberImage;
import com.noname.entity.Product;
import com.noname.entity.ProductImage;
import com.noname.enums.AuctionStatus;
import com.noname.enums.ProductStatus;
import com.noname.util.DateConverter;
import com.noname.util.NumberConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberProductDTO {

    private Long productId;
    private String productName;
    private String startPrice;
    private String currentPrice;
    private String startTime;
    private String endTime;
    private String image;
    private ProductStatus productStatus;
    private AuctionStatus auctionStatus;


    public static MemberProductDTO convertToDTO(Product product, Integer bidPrice){
        return MemberProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .auctionStatus(product.getProductAuction().getStatus())
                .productStatus(product.getStatus())
                .startPrice(NumberConverter.formatNumberWithCommas(product.getProductAuction().getStartPrice()))
                .currentPrice(NumberConverter.formatNumberWithCommas(bidPrice))
                .startTime(DateConverter.formatToYYYYMMDDHHMM((product.getProductAuction().getStartTime())))
                .endTime(DateConverter.formatToYYYYMMDDHHMM(product.getProductAuction().getEndTime()))
                .image(convertImage(product.getProductImages().get(0)))
                .build();
    }
    


    private static String convertImage(ProductImage image) {
    	if(image != null) {
	        return encodeImageURL(image.getPath() + File.separator + image.getIdentifier() + image.getName());
	           
    	}
    	return null;
    }
    
    private static String convertImage(MemberImage image) {
    	if(image != null) {
            return encodeImageURL(image.getPath() + File.separator + image.getIdentifier() + image.getName());
    	}
    	return null;

    }
    
    

    
    
    
    private static String encodeImageURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding image URL");
        }
    }

    


    private static List<ResponseBidDTO> convertBids(List<Bidding> biddings) {
        return biddings != null ?
            biddings.stream()
                .sorted(Comparator.comparingLong(Bidding::getBidId).reversed())
                .limit(5)
                .map(m -> new ResponseBidDTO(
                    m.getMember().getName(),
                    NumberConverter.formatNumberWithCommas(m.getBidPrice()),
                    DateConverter.formatToYYYYMMDD(m.getCreateDate())))
                .collect(Collectors.toList())
            : Collections.emptyList();
    }

}
