package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;

import java.util.Map;

public interface MemberService {
    public Long idDupChk(String memId);
    public Map<String, Object> sendVerifyCode(String memEmail);
    public Members findByMemId(String memId);
    public String signup(MemberDto memberDto);
    public int countByMemEmail(String memEmail);
    public Map<String, Object> sendIdList(String memEmail);
    public Map<String, Object> sendTempPw(String memId, String memEmail);
    public MemberDto getUserInfo(String memId);
    public boolean currPwChkd(String memId, String memPw);
    public Map<String, Object> settings(MemberDto memberDto);
    public Map<String, Object> delete(String memId);
}
