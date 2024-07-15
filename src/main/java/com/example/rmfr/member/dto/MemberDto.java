package com.example.rmfr.member.dto;

import com.example.rmfr.member.entity.Members;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MemberDto {
    private String memId;
    private String memPw;
    private String memEmail;
    private Integer memLevel;
    private LocalDateTime memPwUpdateDate;
    private String memPhone;
    private String zipcode;
    private String memAddr1;
    private String memAddr2;


}
