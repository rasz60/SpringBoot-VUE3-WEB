package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import org.springframework.stereotype.Service;

public interface MemberService {

    public Long idDupChk(String memId);
    public String signup(MemberDto memberDto);
}
