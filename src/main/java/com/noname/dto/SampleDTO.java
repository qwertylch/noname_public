package com.noname.dto;

import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SampleDTO {

    //product
    private Long productId;
    private String productName;

    //productImage
    private String productImage;

    //member
    private String memberName;

    //aution
    private Long originalPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public SampleDTO(Long productId, String productName, Member member, ProductImage productImage, ProductAuction productAuction) {
        this.productId = productId;
        this.productName = productName;
        this.memberName = member.getName();
        this.originalPrice = productAuction.getStartPrice();
        this.startDate = productAuction.getStartTime();
        this.endDate = productAuction.getEndTime();
        this.productImage = productImage.getPath() + "\\"
                 + productImage.getIdentifier() + productImage.getName();
    }









}
