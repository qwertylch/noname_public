package com.noname.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.noname.entity.Favorite;
import com.noname.entity.Member;
import com.noname.entity.Product;


public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
	
    public boolean existsByProductAndMember(@Param("product") Product product, @Param("member") Member member);
    
    public Optional<Favorite> findByProductAndMember(@Param("product") Product product, @Param("member") Member member);
    
    public void deleteByProductAndMember(@Param("product") Product product, @Param("member") Member member);
    
    @Query("SELECT p FROM Favorite f JOIN f.product p WHERE f.member = :member ORDER BY f.favoriteId ASC")
    public List<Product> findFavoriteByMember(@Param("member") Member member);

    public List<Favorite> findByMember(@Param("member") Member member);
    
    
   
    
    
}