package com.example.rmfr.member.repository;

import com.example.rmfr.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Members, Long> {

    Long countByMemId(String memId);
}
