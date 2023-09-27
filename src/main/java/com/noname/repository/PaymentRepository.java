package com.noname.repository;

import com.noname.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.member.memberId = :sid")
    List<Payment> findAllBySid(@Param("sid") Long sid);

    Payment findByOrderId(String orderId);
}
