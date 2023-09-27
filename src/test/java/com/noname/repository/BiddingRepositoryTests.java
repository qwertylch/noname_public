package com.noname.repository;

import com.noname.dto.MaxBidPrice;
import com.noname.entity.Member;
import com.noname.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class BiddingRepositoryTests {

    @Autowired
    BiddingRepository biddingRepository;
    
    @Autowired
    ProductRepository productRepository;

//    @Autowired
//    SampleRepository2 sampleRepository2;


    @Test
    public void findMaxBidPriceAndMemberByProduct() {


        Product product = productRepository.findById(20L).get();
        
        List<MaxBidPrice> max1 = biddingRepository.findMaxBidMemberByProduct(product);
        Optional<Integer> max2 = biddingRepository.findMaxPriceByProduct(product);
        
        max1.stream().forEach(o -> log.info(o.toString()));
        if(max2.isPresent()) {
        	log.info("Max Price : {} ", max2.get());
        }
        
    }







}