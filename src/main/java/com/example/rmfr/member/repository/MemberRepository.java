package com.example.rmfr.member.repository;

import com.example.rmfr.member.entity.Members;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Long countByMemId(String memId);
    public Optional<Members> findByMemId(String username);
    public Long countByMemEmail(String memEmail);
    public List<Members> findByMemEmail(String memEmail);
}
