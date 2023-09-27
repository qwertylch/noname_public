package com.noname.repository;

import com.noname.entity.Product;
import com.noname.entity.ProductAuction;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductAuctionRepository extends JpaRepository<ProductAuction,Long> {

    @Query("SELECT MIN(p.startPrice) FROM ProductAuction p")
    Integer findTopByOrderByStartPriceAsc();


}
