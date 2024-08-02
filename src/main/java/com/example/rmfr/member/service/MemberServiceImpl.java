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

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final MailUtils mailUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Long idDupChk(String memId) {
        return memberRepository.countByMemIdAndMemDelYn(memId, "N");
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
            Members member = Members.builder().memberDto(memberDto).build();
            member.setMemRegDate(LocalDateTime.now());
            rst = memberRepository.save(member).getMemId();
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
        Members member = memberRepository.findByMemIdAndMemDelYn(username, "N")
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return member;
    }

    @Override
    public int countByMemEmail(String memEmail) {
        int cnt = 0;
        try {
            cnt = memberRepository.countByMemEmailAndMemDelYn(memEmail, "N").intValue();
            if ( cnt <= 0 ) throw new Exception("MEM_EMAIL NOT FOUND.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cnt;
    }

    @Override
    public Map<String, Object> sendIdList(String memEmail) {
        Map<String, Object> rst = null;
        try {
            List<Members> members = memberRepository.findByMemEmailAndMemDelYn(memEmail, "N");

            if ( members.isEmpty() )
                throw new Exception("MEM_EMAIL NOT FOUND.");

            List<String> ids = idBlind(members);
            rst = mailUtils.sendIdToEmail(memEmail, ids);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @Override
    public Map<String, Object> sendTempPw(String memId, String memEmail) {
        Map<String, Object> rst = new HashMap<>();
        try {
            Optional<Members> mem = memberRepository.findByMemIdAndMemDelYn(memId, "N");

            if ( mem.isPresent() ) {
                Members member = mem.get();

                if ( memEmail.equals(member.getMemEmail()) ) {
                    String pw = tempPwGenerate();
                    member.setMemPw(bCryptPasswordEncoder.encode(pw));
                    memberRepository.save(member);
                    rst = mailUtils.sendPwToEmail(member.getMemEmail(), pw);
                } else {
                    rst.put("resultCode", 400);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @Override
    public MemberDto getUserInfo(String memId) {
        MemberDto member = new MemberDto();
        try {
            Optional<Members> mem = memberRepository.findByMemIdAndMemDelYn(memId, "N");

            if ( mem.isPresent() ) {
                member.of(mem.get());
            } else {
                throw new Exception("MEM_ID NOT FOUND.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return member;
    }

    @Override
    public boolean currPwChkd(String memId, String memPw) {
        boolean rst = false;
        try {
            Optional<Members> mem = memberRepository.findByMemIdAndMemDelYn(memId, "N");

            if ( mem.isPresent() ) {
                rst = bCryptPasswordEncoder.matches(memPw, mem.get().getMemPw());
            } else {
                throw new Exception("MEM_ID NOT FOUND.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @Transactional
    @Override
    public Map<String, Object> settings(MemberDto memberDto) {
        Map<String, Object> rst = new HashMap<>();
        try {
            Optional<Members> mem = memberRepository.findByMemIdAndMemDelYn(memberDto.getMemId(), "N");

            if ( mem.isPresent() ) {

                if ( !"".equals(memberDto.getMemPw()) )
                    memberDto.setMemPw(bCryptPasswordEncoder.encode(memberDto.getMemPw()));

                Members member = mem.get();
                member.of(memberDto);
                memberRepository.save(member);
                rst.put("resultCode", "200");
            } else {
                rst.put("resultCode", "400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.put("resultCode", "500");
        }
        return rst;
    }

    @Transactional
    @Override
    public Map<String, Object> delete(String memId) {
        Map<String, Object> rst = new HashMap<>();
        try {
            Optional<Members> mem = memberRepository.findByMemIdAndMemDelYn(memId, "N");
            if (mem.isPresent() ) {
                Members member = mem.get();
                member.setMemDelYn("Y");
                member.setMemDelDate(LocalDateTime.now());
                memberRepository.save(member);
               rst.put("resultCode", "200");
            } else {
                rst.put("resultCode", "400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.put("resultCode", "500");
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

    public String tempPwGenerate() {
        String pw = "";
        String[] regStr = {"`", "~", "!", "@", "$", "%", "*", "#", "^", "?", "&", "(", ")", "-", "_", "=", "+"};

        for ( int i = 0; i < 15; i++ ) {
            int random = (int)Math.ceil(Math.random() * 10);

            if (random < 3) { // 1, 2
                pw += (int)(Math.random() * 10) + "";
            } else if (random < 6) { // 3, 4, 5
                char key = (char) ((Math.random() * 26) + 65);
                pw += key + "";
            } else if (random < 9) { // 6, 7, 8
                char key = (char) ((Math.random() * 26) + 97);
                pw += key + "";
            } else { // 9, 10
                int idx = (int)(Math.random() * regStr.length);
                pw += regStr[idx];
            }
        }
        return pw;
    }
}
