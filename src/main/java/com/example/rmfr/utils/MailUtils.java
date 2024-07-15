package com.example.rmfr.utils;

import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;

@Component
public class MailUtils {
    @Autowired
    private JavaMailSender sender;
    public Map<String, Object> sendEmail(String toAddress, String code) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String base64ValidCode = Base64Util.encode(code);
        result.put("token", base64ValidCode);
        String subject = "`rmfr?`에서 보낸 인증 메일입니다.";

        try {

            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText("rmfr 인증 메일입니다.\nrmfr 화면으로 돌아가 아래 인증코드를 입력해주세요.\n\n인증코드 : "+ code);
            helper.setFrom("rmfr <rmfr@gmail.com>");
            result.put("resultCode", 200);
            result.put("token", base64ValidCode);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }
        sender.send(message);
        return result;
    }
}
