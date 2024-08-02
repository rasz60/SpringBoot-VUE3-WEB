package com.example.rmfr.config.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errMsg = "가입되지 않은 아이디입니다.";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if(exception instanceof BadCredentialsException) {
            errMsg = "아이디나 비밀번호를 다시 확인해주세요.";
        } else if(exception instanceof DisabledException) {
            errMsg = "오랜 시간 접속하지 않아 잠긴 계정입니다.";
        } else if(exception instanceof CredentialsExpiredException) {
            errMsg = "비밀번호가 만료되었습니다.";
        }

        objectMapper.writeValue(response.getWriter(), errMsg);
    }
}