package com.noname.repository;

import com.noname.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SampleJpa extends JpaRepository<Member, Long> {
}
