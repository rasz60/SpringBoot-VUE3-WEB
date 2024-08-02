package com.example.rmfr.member.repository;

import com.example.rmfr.member.entity.Members;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Long countByMemIdAndMemDelYn(String memId, String memYN);
    public Optional<Members> findByMemIdAndMemDelYn(String username, String memYN);
    public Long countByMemEmailAndMemDelYn(String memEmail, String memDelYn);
    public List<Members> findByMemEmailAndMemDelYn(String memEmail, String memDelYn);
}
