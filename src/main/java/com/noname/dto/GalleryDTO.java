package com.noname.dto;


import com.noname.entity.Member;
import com.noname.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@NoArgsConstructor
public class GalleryDTO {
    private Long productId;
    private String productImage;
    private String memberName;
    private String productName;


    // Entity -> DTO
    public  GalleryDTO (Product product) {
        this.productId = product.getProductId();
        this.productImage = URLEncoder.encode(
        		product.getProductImages().get(0).getPath() + File.separator
              + product.getProductImages().get(0).getIdentifier() + product.getProductImages().get(0).getName(), StandardCharsets.UTF_8);
        // member 필드는 ManyToOne 관계로 설정되어 있으므로, memberName을 GalleryDTO에 가져오려면 getMember() 해서 가져와야된다.
        this.memberName = product.getMember().getName();
        // 제품 이름 가져오기
        this.productName = product.getName();
        
//        widow
//        URLEncoder.encode(
//		product.getProductImages().get(0).getPath() + File.separator
//        + product.getProductImages().get(0).getIdentifier() + product.getProductImages().get(0), StandardCharsets.UTF_8);
//        mac
//        product.getProductImages().get(0).getPath() + File.separator
//        + product.getProductImages().get(0).getIdentifier() + product.getProductImages().get(0).getName();

    }



}
