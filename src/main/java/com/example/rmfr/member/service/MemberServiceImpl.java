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

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

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
            // spring security 비밀번호 암호화 적용
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

    @Override
    public int countByMemEmail(String memEmail) {
        int cnt = 0;
        try {
            cnt = memberRepository.countByMemEmail(memEmail).intValue();
            if ( cnt <= 0 ) throw new Exception("MEM_EMAIL NOT FOUND.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cnt;
    }

    @Override
    public List<Members> findByMemEmail(String memEmail) {
        List<Members> members = null;
        try {
            members = memberRepository.findByMemEmail(memEmail);
            if ( members.isEmpty() ) throw new Exception("MEM_EMAIL NOT FOUND.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return members;
    }

    @Override
    public Map<String, Object> sendIdList(String memEmail) {
        Map<String, Object> rst = null;
        try {
            List<Members> members = this.findByMemEmail(memEmail);
            List<String> ids = idBlind(members);
            rst = mailUtils.sendIdToEmail(memEmail, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    public List<String> idBlind(List<Members> members) {
        List<String> ids = new ArrayList<>();
        for ( Members member : members ) {
            String memId = member.getMemId();

            String blind = memId.substring(0, 4);
            int times = memId.length() - 4;
            for (int i = 0; i < times; i++ ) {
                blind += "*";
            }

            ids.add(blind);
        }

        return ids;
    }
}
