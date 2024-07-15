package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface MemberService {

    public Long idDupChk(String memId);
    public String signup(MemberDto memberDto);
    public Map<String, Object> sendVerifyCode(String memEmail);
}
