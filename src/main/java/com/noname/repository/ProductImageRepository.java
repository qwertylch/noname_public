package com.noname.repository;

import com.noname.entity.Product;
import com.noname.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
