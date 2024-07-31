package com.example.rmfr.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Component
public class MailUtils {
    @Autowired
    private JavaMailSender sender;
    public Map<String, Object> sendEmail(String toAddress, String code) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String subject = "`rmfr?`에서 보낸 인증 메일입니다.";

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText("rmfr 인증 메일입니다.\nrmfr 화면으로 돌아가 아래 인증코드를 입력해주세요.\n\n인증코드 : "+ code);
            helper.setFrom("rmfr <rmfr@gmail.com>");
            result.put("resultCode", 200);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }
        sender.send(message);
        return result;
    }
    
    public Map<String, Object> sendIdToEmail(String toAddress, List<String> ids) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String subject = "`rmfr?`에서 보낸 아이디 찾기 결과 메일입니다.";
        String text = "안녕하세요, `rmfr?` 입니다.\n"+ toAddress + "로 가입된 아이디입니다.\n\n";
        for (String id : ids ) {
            text += id + "\n";
        }

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom("rmfr <rmfr@gmail.com>");
            result.put("resultCode", 200);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }
        sender.send(message);
        return result;
    }

    public Map<String, Object> sendPwToEmail(String toAddress, String pw) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String subject = "`rmfr?`에서 보낸 비밀번호 찾기 결과 메일입니다.";
        String text = "안녕하세요, `rmfr?` 입니다.\n임시로 발급된 패스워드 안내드립니다.\n\n" + pw + "\n\n임시 비밀번호는 로그인 후 꼭 변경하여 사용해주시길 바랍니다.";

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom("rmfr <rmfr@gmail.com>");
            result.put("resultCode", 200);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }
        sender.send(message);
        return result;
    }
}
