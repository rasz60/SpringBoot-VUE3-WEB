package com.example.rmfr.member.service;

import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.repository.MemberRepository;
import com.example.rmfr.utils.MailUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final MailUtils mailUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Long idDupChk(String memId) {
        return memberRepository.countByMemId(memId);
    }

    @Override
    public Map<String, Object> sendVerifyCode(String memEmail) {
        Map<String, Object> rst = null;
        String code = createVerifyCode();
        try {
            rst = mailUtils.sendEmail(memEmail, code);

            if ( (int) rst.get("resultCode") == 200 ) {
                String base64ValidCode = Base64Util.encode(code);
                rst.put("token", base64ValidCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }


    @Transactional
    @Override
    public String signup(MemberDto memberDto) {
        String rst = "";
        try {
            memberDto.setMemPw(bCryptPasswordEncoder.encode(memberDto.getMemPw()));
            rst = memberRepository.save(Members.builder().memberDto(memberDto).build()).getMemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    public String createVerifyCode() {
        String code = "";

        for ( int i = 0; i < 6; i++ ) {
            if (Math.random() * 10 > 4) {
                code += (int)(Math.random() * 10) + "";
            } else {
                char key = (char) ((Math.random() * 26) + 65);
                code += key + "";
            }
        }

        return code;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Members member = memberRepository.findByMemId(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return member;
    }


}
