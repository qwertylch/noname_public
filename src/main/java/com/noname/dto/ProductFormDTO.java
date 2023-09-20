package com.noname.dto;

import com.noname.entity.Bidding;
import com.noname.entity.Product;
import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@ToString // 스트링타입으로 변형해서 출력
public class ProductFormDTO {
	
	private Long productId;
	
    private String path;
    private String identifier;
    private String name;

    // Product
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
    private List<MultipartFile> imageFiles;
    private String productName;
    

    // ProductAuction
    private String price;

    // ProductAuction
    private String startTime;
    private String endTime;

    // DB에 저장 처리만 용
    // DTO -> Entity
//    public Product toEntity(){
//        // Product
//        String bulk = this.width+"*"+this.depth +"*"+this.height;
//        Product pro = new Product();
//        pro.setType(this.type);
//        pro.setShape(this.shape);
//        pro.setColor(this.color);
//        pro.setBulk(bulk);
//        pro.setExplanation(this.explanation);
//
//        // ProductAuction
//        ProductAuction auc = new ProductAuction();
//
//        pro.setProductAuction(auc);
//
//
//
//
//        return pro;
//    }


}
