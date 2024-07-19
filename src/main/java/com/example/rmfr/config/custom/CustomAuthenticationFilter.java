package com.example.rmfr.config.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

// Security 인증 단계 custom
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter() {
        // url과 일치할 경우 필터가 동작
        super(new AntPathRequestMatcher("/rest/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 1. HTTP.POST 체크
        if(!"POST".equals(request.getMethod())) {
            throw new IllegalStateException("Authentication is not supported");
        }

        // 2. loginDto 매핑
        LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);

        // 3. ID, PASSWORD 가 있는지 확인
        if(!StringUtils.hasLength(loginDto.getMemId()) || !StringUtils.hasLength(loginDto.getMemPw())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        // 4. 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(loginDto.getMemId(), loginDto.getMemPw());

        // 5. manager에게 토큰 인증 처리
        Authentication authenticate = getAuthenticationManager().authenticate(token);

        return authenticate;
    }

    // 내부 클래스 loginDto
    @Data
    public static class LoginDto {
        private String memId;
        private String memPw;
    }
}
