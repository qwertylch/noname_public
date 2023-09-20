package com.noname.repository;

import com.noname.entity.Product;
import com.noname.entity.ProductAuction;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAuctionRepository extends JpaRepository<ProductAuction,Long> {


	public Optional<ProductAuction> findTopByOrderByStartPriceAsc(); 


}
