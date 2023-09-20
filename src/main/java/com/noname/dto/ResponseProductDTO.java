package com.noname.dto;


import com.noname.entity.Bidding;
import com.noname.entity.MemberImage;
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
public class ResponseProductDTO {

    private Long productId;
    private String productName;
    private String explanation;
    private String type;
    private String shape;
    private String bulk;
    private String mnfctDate;
    private String auctionStatus;
    private String startPrice;
    private String startTime;
    private String endTime;
    private Long startDiff;
    private Long endDiff;
    private List<String> images;
    private String memberName;
    private String memberEmail;
    private String memberImage;
    private List<ResponseBidDTO> bids;
    private boolean favorite;


    public static ResponseProductDTO convertToDTO(Product product){
        return ResponseProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .explanation(product.getExplanation())
                .type(product.getType())
                .shape(product.getShape())
                .bulk(product.getBulk())
                .mnfctDate(DateConverter.formatToYYYY(product.getMnfctDate()))
                .auctionStatus(product.getProductAuction().getStatus().name())
                .startPrice(NumberConverter.formatNumberWithCommas(product.getProductAuction().getStartPrice()))
                .startTime(DateConverter.formatToMMDDAHHMM(product.getProductAuction().getStartTime()))
                .endTime(DateConverter.formatToMMDDAHHMM(product.getProductAuction().getEndTime()))
                .startDiff(DateConverter.timeDifferenceToMillis(product.getProductAuction().getStartTime()))
                .endDiff(DateConverter.timeDifferenceToMillis(product.getProductAuction().getEndTime()))
                .memberName(product.getMember().getName())
                .memberEmail(product.getMember().getEmail())
                .images(convertImages(product.getProductImages()))
                .memberImage(convertImage(product.getMember().getImages()))
                .bids(convertBids(product.getBiddings()))
                .build();
    }
    


    private static List<String> convertImages(List<ProductImage> images) {
    	if(images != null) {
	        return images.stream()
	            .map(image -> encodeImageURL(image.getPath() + File.separator + image.getIdentifier() + image.getName()))
	            .collect(Collectors.toList());
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
