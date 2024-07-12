package com.example.rmfr.member.controller;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/signup/idDupChk/{memId}")
    public Long idDupChk(@PathVariable("memId") String memId) {
        return memberService.idDupChk(memId);
    }


    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody MemberDto memberDto) {
        return memberService.signup(memberDto);
    }
}
