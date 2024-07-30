package com.example.rmfr.member.controller;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/rest/signup/idDupChk/{memId}")
    public Long idDupChk(@PathVariable("memId") String memId) {
        return memberService.idDupChk(memId);
    }
    @GetMapping("/rest/signup/verifyCode/{memEmail}")
    public Map<String, Object> verifyCode(@PathVariable("memEmail") String memEmail) {
        return memberService.sendVerifyCode(memEmail);
    }
    @PostMapping("/rest/signup")
    @ResponseBody
    public String signup(@RequestBody MemberDto memberDto) {
        return memberService.signup(memberDto);
    }

    @GetMapping("/rest/findId/{memEmail}")
    @ResponseBody
    public int findId(@PathVariable("memEmail") String memEmail) {
        return memberService.countByMemEmail(memEmail);
    }
    @GetMapping("/rest/sendIdList/{memEmail}")
    public Map<String, Object> sendIdList(@PathVariable("memEmail") String memEmail) {
        return memberService.sendIdList(memEmail);
    }

    @GetMapping("/rest/sendTempPw/{memEmail}")
    public Map<String, Object> sendTempPw(@PathVariable("memEmail") String memEmail) {
        return memberService.sendTempPw(memEmail);
    }
}
