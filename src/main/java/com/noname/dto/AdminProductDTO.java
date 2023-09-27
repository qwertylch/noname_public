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
public class AdminProductDTO {

    private Long productId;
    private String productName;
    private String type;
    private String memberName;
    private String startTime;
    private String endTime;
    private String createTime;
    private String productStatus;
    private String auctionStatus;

    public static AdminProductDTO convertToDTO(Product product){
        return AdminProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .type(product.getType())
                .memberName(product.getMember().getName())    
                .startTime(DateConverter.formatTimeAgoHourOrDate(product.getProductAuction().getStartTime()))
                .endTime(DateConverter.formatTimeAgoHourOrDate(product.getProductAuction().getEndTime()))
                .createTime(DateConverter.formatTimeAgoHourOrDate(product.getCreateDate()))
                .auctionStatus(product.getProductAuction().getStatus().getTitle())
                .productStatus(product.getStatus().getTitle())
                .build();
    }
    



    

    

    




}
