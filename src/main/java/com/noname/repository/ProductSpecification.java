package com.noname.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.criteria.*;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.noname.entity.Bidding;
import com.noname.entity.Product;
import com.noname.entity.ProductAuction;

public class ProductSpecification {
	

    public static Specification<Product> containsName(String name) {
        return (root, query, criteriaBuilder) ->    criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> equalType(String type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Product> equalShape(String shape) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shape"), shape);
    }

    public static Specification<Product> equalBulk(String bulk) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bulk"), bulk);
    }
    
    public static Specification<Product> equalColor(String color) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("color"), color);
    }
    
    public static Specification<Product> betweenPrice(Integer startPrice, Integer endPrice) {
        return (Specification<Product>) (root, query, builder) -> {
            if (startPrice == null && endPrice == null) {
                return null;
            }

            Join<Product, ProductAuction> productAuctionJoin = root.join("productAuction", JoinType.LEFT);

            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<Bidding> subRoot = subquery.from(Bidding.class);
            subquery.select(builder.coalesce(builder.max(subRoot.get("bidPrice")), productAuctionJoin.get("startPrice")))
                    .where(builder.equal(subRoot.get("product"), root.get("productId")))
                    .groupBy(subRoot.get("product"));

            Expression<BigDecimal> currentPrice = builder.coalesce(subquery, productAuctionJoin.get("startPrice"));

            Predicate startPriceCondition = builder.greaterThanOrEqualTo(currentPrice, BigDecimal.valueOf(startPrice));
            Predicate endPriceCondition = builder.lessThanOrEqualTo(currentPrice, BigDecimal.valueOf(endPrice));

            return builder.and(startPriceCondition, endPriceCondition);
        };
    }
    
    public static Specification<Product> sortByCreateDateDesc() {
        return (Specification<Product>) (root, query, builder) -> {
            query.orderBy(builder.desc(root.get("createDate")));
            return query.getRestriction();
        };
    }
    
    public static Specification<Product> sortByAuctionStartAsc() {
        return (root, query, builder) -> {
            Join<Product, ProductAuction> productAuctionJoin = root.join("productAuction");

            LocalDateTime currentDateTime = LocalDateTime.now();
            query.where(builder.or(
                    builder.greaterThanOrEqualTo(productAuctionJoin.get("startTime"), currentDateTime),
                    builder.isNull(productAuctionJoin.get("startTime"))
            ));

            query.orderBy(builder.asc(productAuctionJoin.get("startTime"))); // 변경된 부분

            return null;
        };
    }
    
    
    public static Specification<Product> sortByAuctionEndAsc() {
        return (root, query, builder) -> {
            Join<Product, ProductAuction> productAuctionJoin = root.join("productAuction");

            LocalDateTime currentDateTime = LocalDateTime.now();
            query.where(builder.or(
                    builder.greaterThanOrEqualTo(productAuctionJoin.get("endTime"), currentDateTime),
                    builder.isNull(productAuctionJoin.get("endTime"))
            ));

            query.orderBy(builder.asc(productAuctionJoin.get("endTime")));

            return null;
        };
    }






    
//    public static Specification<Product> equalType(List<String> types) {
//        return (root, query, criteriaBuilder) -> root.get("type").in(types);
//    }

    		
    		

}
