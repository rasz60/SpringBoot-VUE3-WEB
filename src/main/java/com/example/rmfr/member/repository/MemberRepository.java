package com.example.rmfr.member.repository;

import com.example.rmfr.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {

    Long countByMemId(String memId);

    public Optional<Members> findByMemId(String username);
}
