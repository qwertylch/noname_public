package com.noname.repository;



import com.noname.entity.Product;
import com.noname.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class SampleRepository {

    private final EntityManager entityManager;

    @Autowired
    public SampleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Product> findAllProduct() {
        List<Product> foundProducts = entityManager.createQuery("SELECT p FROM Product p ORDER BY p.productId ASC", Product.class)
                .getResultList();
        return foundProducts;
    }


//
//    public Long saveProductImage(ProductImage productImage){
//        entityManager.persist(productImage);
//        return productImage.getProduct().getProductId();
//    }
//
//    public List<ProductImage> findImageByProductId(Long productId) {
//        return entityManager.createQuery("select i from ProductImage i where i.product_id = :pid", ProductImage.class)
//                .setParameter("pid", productId)
//                .getResultList();
//    }




}
