package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Long idDupChk(String memId) {
        return memberRepository.countByMemId(memId);
    }

    @Transactional
    @Override
    public String signup(MemberDto memberDto) {
        String rst = "";
        try {
            rst = memberRepository.save(Members.builder().memberDto(memberDto).build()).getMemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }
}
