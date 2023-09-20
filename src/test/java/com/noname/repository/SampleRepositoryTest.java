package com.noname.repository;

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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SampleRepositoryTest {

    @Autowired
    SampleRepository sampleRepository;

//    @Autowired
//    SampleRepository2 sampleRepository2;


    @Test
    @Transactional
    public void testSaveProductWithMember() {

        Member member = new Member();
        member.setEmail("sample@example.com");
        member.setName("Sample Member");
//        member.set
//        memberRepository.save(member);


        Product product = new Product();
        product.setName("product1");
        product.setExplanation("test");
        product.setType("type");
        product.setShape("10*10");
        product.setBulk("bulk");
        product.setColor("red");
        product.setMnfctDate(LocalDateTime.now());
        product.setMember(member);

//        productRepository.saveProduct(product);

        // Further testing assertions
    }


    @Test
    void findAllProduct() {
        List<Product> allProduct = sampleRepository.findAllProduct();


        log.info("findAllProduct : {}",  allProduct);
//        Product product= allProduct.get(0);
//        log.info("findProduct ======================================: {}",  product.toString());
//        log.info("findProduct ======================================: {}",  product.getProductAuction().toString());
    }

//    @Test
//    void findAllProduct2() {
//        List<Product> allProduct = sampleRepository2.findAll();

//        log.info("findAllProduct : {}",  allProduct);
//        Product product= allProduct.get(0);
//        log.info("findProduct : {}",  product.toString());
//    }





}