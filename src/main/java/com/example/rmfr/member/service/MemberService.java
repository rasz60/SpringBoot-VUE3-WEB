package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberService {
    public Long idDupChk(String memId);
    public Map<String, Object> sendVerifyCode(String memEmail);
    public String signup(MemberDto memberDto);
    public int countByMemEmail(String memEmail);
    public List<Members> findByMemEmail(String memEmail);
    public Map<String, Object> sendIdList(String memEmail);
    public Map<String, Object> sendTempPw(String memEmail);
}
