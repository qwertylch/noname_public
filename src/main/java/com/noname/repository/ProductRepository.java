package com.noname.repository;

import com.noname.entity.Bidding;
import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.enums.ProductStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
	
	
	public List<Product> findAll(Specification<Product> spec);
	
	@Query("SELECT p FROM Product p ORDER BY p.mnfctDate DESC")
	public List<Product> getByMnfctDateDesc();
	 
	@Query("SELECT p FROM Product p ORDER BY p.mnfctDate ASC")
	public List<Product> getByMnfctDateAsc(); 
	
	public List<Product> findByStatus(ProductStatus status);
    
	public List<Product> findByMember(@Param("Member") Member member);
	 
//    public Product findProductById(Long productId) {
//        Product product = entityManager.find(Product.class, productId);
//        return product;
//    }
//
//
//    // 전체 객체 찾아오기
//    public List<Product> getAllProduct(){
//        List<Product> products = entityManager.createQuery(
//                "SELECT p FROM Product p",
//                Product.class)
//                .getResultList();
//        return products;
//    }
//
//    public List<Product> getByMnfctDateDesc() {
//        List<Product> products = entityManager.createQuery(
//                        "SELECT p FROM Product p ORDER BY p.mnfctDate DESC",
//                        Product.class)
//                .getResultList();
//        return products;
//    }
//
//    public List<Product> getByMnfctDateAsc(){
//        List<Product> products = entityManager.createQuery(
//                        "SELECT p FROM Product p ORDER BY p.mnfctDate ASC",
//                        Product.class)
//                .getResultList();
//        return products;
//    }
	
	
	
}
